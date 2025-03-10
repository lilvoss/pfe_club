package tn.esprit.pfe_club.listener

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.util.regex.Pattern

class SMSBroadcastReceiver : BroadcastReceiver() {

    var otpListener: ((String) -> Unit)? = null

    override fun onReceive(context: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status = extras?.get(SmsRetriever.EXTRA_STATUS) as? Status
            when (status?.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    // Retrieve the full SMS message
                    val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                    Log.d("SMSBroadcastReceiver", "📩 SMS Received: $message")

                    // Extract only the first four digits from the beginning of the message
                    val pattern = Pattern.compile("^(\\d{4})") // Matches the first 4 digits
                    val matcher = pattern.matcher(message)

                    if (matcher.find()) {
                        val otp = matcher.group(1)!! // Get the OTP (first 4 digits)
                        Log.d("SMSBroadcastReceiver", "✅ Extracted OTP: $otp")
                        otpListener?.invoke(otp) // Pass OTP to the listener
                    } else {
                        Log.e("SMSBroadcastReceiver", "❌ No OTP found in message!")
                    }
                }
                CommonStatusCodes.TIMEOUT -> {
                    Log.e("SMSBroadcastReceiver", "⏳ SMS Retrieval Timed Out")
                }
            }
        }
    }
}
