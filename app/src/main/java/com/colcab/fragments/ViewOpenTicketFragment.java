package com.colcab.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.colcab.MainActivity;
import com.colcab.R;
import com.colcab.adapters.ViewTicketPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ViewOpenTicketFragment extends Fragment {

    public static final String TICKET_ID = "ticketID";
    public static String ticketID;
    public static boolean condition;

    public ViewOpenTicketFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        condition = false;
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ticketID = getArguments().getString(TICKET_ID);
        getTicketInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_open_ticket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewTicketPagerAdapter adapter = new ViewTicketPagerAdapter(getChildFragmentManager());
        ViewPager viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.view_ticket_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
            case R.id.view_ticket_delete:
                createDeleteDialog();
                break;
            case R.id.view_ticket_close:
                fullScreenDialog();
                break;
        }
        return true;
    }

    public void getTicketInfo() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("tickets").document(ticketID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    Map<String, Object> data = document.getData();
                    ViewTicketInfoFragment.fillFields(data);
                }
            }
        });
    }

    private void createDeleteDialog() {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Delete Ticket")
                .setMessage("Are You Sure?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Ticket Deleted", Toast.LENGTH_SHORT).show();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("tickets").document(getArguments().getString(TICKET_ID)).delete();
                        Navigation.findNavController(getView()).navigateUp();
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void fullScreenDialog() {
        FullScreenDialogFragment dialog = FullScreenDialogFragment.newInstance();
        dialog.show(getChildFragmentManager(), "TAG");
        dialog.setCallback(new FullScreenDialogFragment.Callback() {
            @Override
            public void onActionClick(HashMap<String, Object> data) {
                Toast.makeText(getContext(), String.valueOf(data.get("data1")), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

