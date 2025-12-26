<?php
header("Content-Type: application/json");

$email = $_POST['email'] ?? '';
$otp = $_POST['otp'] ?? '';

$file = "otp_" . md5($email) . ".txt";

if (!file_exists($file)) {
    echo json_encode([
        "status" => "error",
        "message" => "کدی یافت نشد"
    ]);
    exit;
}

$savedOtp = trim(file_get_contents($file));

if ($otp === $savedOtp) {
    unlink($file); 
    echo json_encode([
        "status" => "success",
        "message" => "احراز هویت موفق"
    ]);
} else {
    echo json_encode([
        "status" => "error",
        "message" => "کد اشتباه است"
    ]);
}
