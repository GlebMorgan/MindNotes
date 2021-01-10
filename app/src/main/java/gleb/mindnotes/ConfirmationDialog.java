package gleb.mindnotes;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ConfirmationDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = ConfirmationDialog.class.getSimpleName();

    public static final int DIALOG_BACK = 0;
    public static final int DIALOG_DEL = 1;

    private TextView titleText;
    private TextView meseageText;
    private TextView okButton;
    private TextView cancelButton;

    private int type;
    private String title;
    private String message;

    interface ConfirmedActionPerformer {
        void confirmedDeleteNote();
        void confirmedExit(); //not used here
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        type = getArguments().getInt("type");
        title = getArguments().getString("title");
        message = getArguments().getString("message");

        View confirmationDialogView = inflater.inflate(R.layout.fragment_confirmation_dialog, null);
        titleText = (TextView) confirmationDialogView.findViewById(R.id.confirmation_dialog_title);
        meseageText = (TextView) confirmationDialogView.findViewById(R.id.confirmation_dialog_message);
        okButton = (TextView) confirmationDialogView.findViewById(R.id.confirmation_ok_button);
        cancelButton = (TextView) confirmationDialogView.findViewById(R.id.confirmation_cancel_button);

        titleText.setText(title);
        meseageText.setText(message);

        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        ((View) okButton).setOnTouchListener(OnTouchViewListener.getInstance());
        ((View) cancelButton).setOnTouchListener(OnTouchViewListener.getInstance());

        return confirmationDialogView;
    }

    @Override
    public void onClick(View view) {
        switch (type) {
            case DIALOG_BACK:
                if (view.getId() == R.id.confirmation_ok_button) {
                    getActivity().finish();
                }
                break;
            case DIALOG_DEL:
                if (view.getId() == R.id.confirmation_ok_button) {
                    try { ((ConfirmedActionPerformer) getActivity()).confirmedDeleteNote(); }
                    catch (ClassCastException e) {
                        Log.e(TAG, "Parent activity should implement " +
                                "ConfirmedActionPerformer interface in order to " +
                                "receive acknowledgements from ConfirmationDialog fragment", e);
                    }
                }
                break;
            default:
                Log.e("TAG", "OnClick handler - Wrong confirmationDialogType");
        }
        dismiss();
    }
}
