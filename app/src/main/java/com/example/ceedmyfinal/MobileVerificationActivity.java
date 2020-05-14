package com.example.ceedmyfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;

public class MobileVerificationActivity extends AppCompatActivity
{

    private PinEntryEditText pinEntryEditTextMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification);

        getSupportActionBar().hide();

        pinEntryEditTextMobile = findViewById(R.id.idPinEntryEditTextMobile);

        String name = SharedPreferenceManager.getInstance(this).getUserName();
        Log.d("TAG", "onCreate: "+name);

        verifyMobile();
    }

    private void verifyMobile()
    {
        if(pinEntryEditTextMobile != null)
        {
            pinEntryEditTextMobile.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener()
            {
                @Override
                public void onPinEntered(CharSequence str)
                {
                    if(str.toString().equals("123456"))
                    {
//                        Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_SHORT).show();
                        showAlertDialogOTP();
                    }
                    else
                    {
                        Toast toast = Toast.makeText(
                                getApplicationContext(),
                                "WRONG OTP, TRY AGAIN...",
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
//                        Toast.makeText(getApplicationContext(), "WRONG OTP, TRY AGAIN", Toast.LENGTH_SHORT).show();
                        pinEntryEditTextMobile.setText(null);
                    }
                }
            });
        }
    }

    private void showAlertDialogOTP()
    {

        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(MobileVerificationActivity.this);

        // Set the message show for the Alert time

        builder.setIcon(R.drawable.ic_smartphone);
        builder.setMessage("VERIFIED SUCCESSFULLY");

        // Set Alert Title
        builder.setTitle("MOBILE NUMBER");

        // Set Cancelable false// Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.

        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Ok",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // When the user click yes button
                                // then app will close
//                                finish();
                                startActivity(new Intent(getApplicationContext(),EmailVerificationActivity.class));

                            }
                        });


        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }
}
