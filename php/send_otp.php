<?php


header("Content-Type: application/json");

$email = $_POST['email'] ?? $_GET['email'] ?? '';

if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    echo json_encode([
        "status" => "error",
        "message" => "ایمیل نامعتبر است"
    ]);
    exit;
}

$otp = rand(100000, 999999);

$file = "otp_" . md5($email) . ".txt";
file_put_contents($file, $otp);

$from_email = "no-reply@mrbackend.ir"; // ایمیل واقعی دامنه خودت
$from_name = "MRBackend";               // نام فرستنده
$subject = "کد ورود شما";

$message = "
<html>
<head>
  <title>کد ورود شما</title>
  <style>
    body { font-family: Tahoma, sans-serif; background-color: #f5f5f5; padding: 20px; }
    .container { background-color: #fff; padding: 30px; border-radius: 10px; text-align: center; box-shadow: 0 0 15px rgba(0,0,0,0.1); }
    h2 { color: #333; }
    p { font-size: 16px; color: #555; }
    .otp { font-size: 28px; font-weight: bold; color: #1a73e8; margin: 20px 0; }
    .footer { font-size: 12px; color: #999; margin-top: 20px; }
  </style>
</head>
<body>
  <div class='container'>
    <h2>سلام</h2>
    <p> MRBackend  |  کد ورود شما به برنامه</p>
    <div class='otp'>$otp</div>
    <p>لطفاً این کد را با کسی به اشتراک نگذارید</p>
    <div class='footer'>© 2025 MRBackend - AmirReza Akbari</div>
  </div>
</body>
</html>
";

$headers = "MIME-Version: 1.0" . "\r\n";
$headers .= "Content-type:text/html;charset=UTF-8" . "\r\n";
$headers .= "From: $from_name <$from_email>" . "\r\n";

if (mail($email, $subject, $message, $headers)) {
    echo json_encode([
        "status" => "success",
        "message" => "کد ارسال شد"
    ]);
} else {
    echo json_encode([
        "status" => "error",
        "message" => "ارسال ایمیل ناموفق بود"
    ]);
}
