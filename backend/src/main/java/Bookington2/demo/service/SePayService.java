package Bookington2.demo.service;

import Bookington2.demo.dto.PaymentResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@Service
public class SePayService {

    // --- SePay ---
    @Value("${sepay.api.url:https://my.sepay.vn/userapi}")
    private String sepayApiUrl;

    @Value("${sepay.api.token:}")
    private String sepayApiToken;

    @Value("${sepay.polling.timeout:900000}")
    private long sepayPollingTimeout;

    // --- Bank/VietQR ---
    @Value("${bank.bin:}")
    private String bankBin;

    @Value("${bank.account.number:}")
    private String bankAccountNumber;

    @Value("${bank.account.name:}")
    private String bankAccountName;

    @Value("${bank.template:compact}")
    private String bankTemplate;

    // --- Polling/Verification ---
    @Value("${payment.polling.interval:3000}")
    private long paymentPollingInterval;

    @Value("${payment.verification.maxRetries:5}")
    private int verificationMaxRetries;

    private final RestTemplate restTemplate = new RestTemplate();

    private String normalizeBaseUrl(String url) {
        if (url == null) return "";
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    public String generateQRCode(String amount, String content, String bookingId) {
        // VietQR image (dạng phổ biến):
        // https://img.vietqr.io/image/{BIN}-{ACCOUNT}-{TEMPLATE}.png?amount=...&addInfo=...&accountName=...
        // (tùy VietQR hỗ trợ param, nhưng đây là format hay dùng)
        String base = "https://img.vietqr.io/image/%s-%s-%s.png";
        String imageUrl = String.format(base, bankBin, bankAccountNumber, bankTemplate);

        return UriComponentsBuilder.fromHttpUrl(imageUrl)
                .queryParam("amount", amount)
                .queryParam("addInfo", content)
                .queryParam("accountName", bankAccountName)
                .build()
                .toUriString();
    }

    public PaymentResponseDTO createPayment(String amount, String content, String bookingId,
                                           String customerName, String phoneNumber) {

        String paymentId = "PAY" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String qrCodeUrl = generateQRCode(amount, content, bookingId);

        // paymentUrl (nếu SePay có endpoint checkout kiểu này thì giữ, còn không thì bạn đổi theo doc SePay)
        String paymentUrl = UriComponentsBuilder
                .fromHttpUrl(normalizeBaseUrl(sepayApiUrl) + "/payment/checkout")
                .queryParam("payment_id", paymentId)
                .queryParam("amount", amount)
                .queryParam("content", content)
                .build()
                .toUriString();

        return PaymentResponseDTO.builder()
                .paymentId(paymentId)
                .bookingId(bookingId)
                .paymentUrl(paymentUrl)
                .qrCodeUrl(qrCodeUrl)
                .status("PENDING")
                .message("Payment initiated. Please scan QR code or use payment link.")
                .bankAccount(bankAccountNumber)
                .bankName(bankBin)           // nếu bạn muốn hiển thị VCB thì tạo thêm bank.shortName
                .accountName(bankAccountName)
                .amount(amount)
                .content(content)
                .expiryTime(System.currentTimeMillis() + (15 * 60 * 1000))
                .build();
    }

    public Map<String, Object> checkPaymentStatus(String paymentId) {
        // Ví dụ URL: {base}/transactions/{paymentId}
        String url = normalizeBaseUrl(sepayApiUrl) + "/transactions/" + paymentId;

        try {
            HttpHeaders headers = new HttpHeaders();
            // tuỳ SePay yêu cầu header gì: Bearer / Token / ApiKey
            headers.set("Authorization", "Bearer " + sepayApiToken);

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            // resp.getBody() là response thực từ SePay
            return resp.getBody();
        } catch (Exception e) {
            return Map.of(
                    "error", "Failed to check payment status",
                    "message", e.getMessage()
            );
        }
    }

    public boolean verifyPayment(String paymentId, String amount, String content) {
        try {
            Map<String, Object> paymentInfo = checkPaymentStatus(paymentId);
            if (paymentInfo == null) return false;

            Object status = paymentInfo.get("status");
            Object paidAmount = paymentInfo.get("amount");
            Object paidContent = paymentInfo.get("content"); // tuỳ field thực tế

            if ("SUCCESS".equals(String.valueOf(status))) {
                boolean okAmount = amount.equals(String.valueOf(paidAmount));
                // nếu bạn muốn check content nữa thì bật:
                // boolean okContent = content.equals(String.valueOf(paidContent));
                return okAmount;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
