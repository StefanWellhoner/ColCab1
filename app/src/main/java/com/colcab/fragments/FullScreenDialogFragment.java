package com.colcab.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.colcab.helpers.Validator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
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

    private TextInputLayout lblAmountDue, lblRootCause, lblClientFeedback, lblSaveAs, lblCategoryType, lblFailureType, lblSatisfactionLevel;
    private AutoCompleteTextView spnCategoryTypes;
    private AutoCompleteTextView spnFailureTypes;
    private AutoCompleteTextView spnCSATs;
    private TextInputEditText tfAmountDue, tfRootCause, tfClientFeedback, tfSaveAs;
    private FirebaseFirestore db;
    ArrayAdapter<String> catOfFailAdapter;
    ArrayList<String> catOfFailList;

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
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_full_screen_dialog, container, false);
        initComponents(linearLayout);
        initSpinners();
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
        lblCategoryType = v.findViewById(R.id.lCategoryType);
        lblFailureType = v.findViewById(R.id.lFailureType);
        lblSatisfactionLevel = v.findViewById(R.id.lSatisfactionLevel);

        tfAmountDue = v.findViewById(R.id.tfAmountDue);
        tfRootCause = v.findViewById(R.id.tfRootCause);
        tfClientFeedback = v.findViewById(R.id.tfClientFeedback);
        tfSaveAs = v.findViewById(R.id.tfSaveAs);

        // Buttons for actions
        Button btnCloseTicket = v.findViewById(R.id.btnCloseTicket);
        ImageButton close = v.findViewById(R.id.fullscreen_dialog_close);

        // Set onClick listeners
        close.setOnClickListener(this);
        btnCloseTicket.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.spnCATSs));
        spnCSATs.setAdapter(adapter);
    }

    private void resetErrors(){
        lblCategoryType.setErrorEnabled(false);
        lblFailureType.setErrorEnabled(false);
        lblAmountDue.setErrorEnabled(false);
        lblRootCause.setErrorEnabled(false);
        lblClientFeedback.setErrorEnabled(false);
        lblSatisfactionLevel.setErrorEnabled(false);
        lblSaveAs.setErrorEnabled(false);
    }

    private boolean isValidData(Map<String, Object> data) {
        int errorCounter = 0;
        resetErrors();
        if (!Validator.isCategoryTypeValid(String.valueOf(data.get("categoryType")))) {
            errorCounter++;
            lblCategoryType.setError("Please Select a Category");
        }
        if (!Validator.isFailureTypeValid(String.valueOf(data.get("failureType")))) {
            errorCounter++;
            lblFailureType.setError("Please Select a Failure type");
        }
        try {
            if (!Validator.isAmountDueValid(Double.parseDouble(String.valueOf(data.get("amountDue"))))) {
                errorCounter++;
                lblAmountDue.setError("Please Enter an Amount due");
            }
        } catch (NumberFormatException ex) {
            errorCounter++;
            lblAmountDue.setError("Please Enter an Amount due");
            System.out.println(ex.getMessage());
        }
        if (!Validator.isRootCauseValid(String.valueOf(data.get("rootCause")))) {
            errorCounter++;
            lblRootCause.setError("Please Enter a Root Cause");
        }
        if (!Validator.isFeedbackValid(String.valueOf(data.get("clientFeedBack")))) {
            errorCounter++;
            lblClientFeedback.setError("Please Enter Client's Feedback");
        }
        try {
            if (!Validator.isSatisfactionValid(Integer.parseInt(String.valueOf(data.get("satisfactionLevel"))))) {
                errorCounter++;
                lblSatisfactionLevel.setError("Please Select the Client's Satisfaction Level");
            }
        } catch (NumberFormatException ex){
            errorCounter++;
            lblSatisfactionLevel.setError("Please Select the Client's Satisfaction Level");
            System.out.println(ex.getMessage());
        }

        if (!Validator.isSaveAsValid(String.valueOf(data.get("saveAs")))) {
            errorCounter++;
            lblSaveAs.setError("Please Enter Save As");
        }
        return !(errorCounter > 0);
    }

    private void initSpinners() {
        catOfFailList = new ArrayList<>();
        catOfFailAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, catOfFailList);
        catOfFailAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategoryTypes.setAdapter(catOfFailAdapter);
        CollectionReference catOfFail = db.collection("categories of failure");
        catOfFail.orderBy("catagoryName", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        catOfFailList.add(document.getString("catagoryName"));
                    }
                    catOfFailAdapter.notifyDataSetChanged();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e.getMessage() != null) {
                    Log.d("Firebase Error: ", e.getMessage());
                    Toast.makeText(getContext(), "An Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private Map<String, Object> getData() {
        String categoryType = String.valueOf(spnCategoryTypes.getText());
        String failureType = String.valueOf(spnFailureTypes.getText());
        String amountDue = String.valueOf(tfAmountDue.getText());
        String rootCause = String.valueOf(tfRootCause.getText());
        String clientFeedBack = String.valueOf(tfClientFeedback.getText());
        String satisfactionLevel = String.valueOf(spnCSATs.getText());
        String saveAs = String.valueOf(tfSaveAs.getText());
        Map<String, Object> data = new HashMap<>();
        data.put("categoryType", categoryType);
        data.put("failureType", failureType);
        data.put("amountDue", amountDue);
        data.put("rootCause", rootCause);
        data.put("clientFeedBack", clientFeedBack);
        data.put("satisfactionLevel", satisfactionLevel);
        data.put("saveAs", saveAs);
        return data;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnCloseTicket:
                Map<String, Object> data = getData();
                if (isValidData(data)) {
                    callback.onActionClick(data);
                    dismiss();
                }
                break;
            case R.id.fullscreen_dialog_close:
                dismiss();
                break;
        }
    }

    public interface Callback {
        void onActionClick(Map<String, Object> data);
    }
}