package com.colcab.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.colcab.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FullScreenDialogFragment extends DialogFragment implements View.OnClickListener {
    private ArrayAdapter<String> failures;
    private ArrayAdapter<String> conAdapter;
    private ArrayList<String> remedialCategory;
    private ArrayList<String> failureTypes;
    private ArrayList<String> clientCSATs;
    private String failureType;
    private String categoryType;
    private String clientCSAT;

    private TextInputLayout lblAmountDue, lblAmountDue1, lblCategoryType, lblFailureType, lblRootCause, lblClientFeedback, lblSaveAs, pageTitle;
    private EditText edtAmountDueCategory, edtAmountDueFailure, edtClientFeedback, edtSaveAs, edtRootCause;
    private Spinner spnCategoryTypes, spnFailureTypes, spnCSATs;
    private Button btnCloseTicket;
    private ImageButton close;

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    static FullScreenDialogFragment newInstance() {
        return new FullScreenDialogFragment();
    }

    public FullScreenDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_PopupOverlay);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_full_screen_dialog, container, false);
        initComponents(linearLayout);
        return linearLayout;
    }

    private void initComponents(View v) {
        // Spinners
        spnFailureTypes = v.findViewById(R.id.spnFailureTypes);
        spnCategoryTypes = v.findViewById(R.id.spnCategoryTypes);
        spnCSATs = v.findViewById(R.id.spnCSAT);

        // Layouts for setting errors
        lblAmountDue = v.findViewById(R.id.lAmountDue);
        lblRootCause = v.findViewById(R.id.lRootCause);
        lblClientFeedback = v.findViewById(R.id.lClientFeedback);
        lblSaveAs = v.findViewById(R.id.lSaveAs);

        // Buttons for actions
        btnCloseTicket = v.findViewById(R.id.btnCloseTicket);
        close = v.findViewById(R.id.fullscreen_dialog_close);

        // Set onClick listeners
        close.setOnClickListener(this);
        btnCloseTicket.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnCloseTicket:
                HashMap<String, Object> data = new HashMap<>();
                data.put("data1", "jfnnvcqwfmqk");
                callback.onActionClick(data);
                dismiss();
                break;
            case R.id.fullscreen_dialog_close:
                dismiss();
                break;
        }
    }

    public interface Callback {
        void onActionClick(HashMap<String, Object> data);
    }
}