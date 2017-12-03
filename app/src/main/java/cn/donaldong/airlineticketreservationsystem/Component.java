package cn.donaldong.airlineticketreservationsystem;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

class Component {
    EditText editText;
    TextView textView;
    String text, error_msg;
    boolean valid;

    Component(EditText editText, TextView textView, String error_msg) {
        this.editText = editText;
        this.textView = textView;
        this.error_msg = error_msg;
        reset();
    }

    void getText() {
        text = editText.getText().toString();
    }

    void setOnFocusChangeListener(View.OnFocusChangeListener l) {
        editText.setOnFocusChangeListener(l);
    }

    void displayError() {
        valid = false;
        editText.setBackgroundResource(R.drawable.edittext_error);
        textView.setText(error_msg);
    }

    void displayError(String error) {
        valid = false;
        editText.setBackgroundResource(R.drawable.edittext_error);
        textView.setText(error);
    }

    void confirm() {
        valid = true;
        editText.setBackgroundResource(R.drawable.edittext_normal);
        textView.setText("");
    }

    void reset() {
        valid = false;
        editText.setBackgroundResource(R.drawable.edittext_normal);
        text = "";
        textView.setText("");
    }

    void clearFocus() {
        editText.clearFocus();
    }

    @Override
    public String toString() {
        return text;
    }
}