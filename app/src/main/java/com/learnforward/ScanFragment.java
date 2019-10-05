package com.learnforward;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class ScanFragment extends Fragment implements BarcodeReader.BarcodeReaderListener{
    private BarcodeReader barcodeReader;
    private Context mContext;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get the barcode reader instance
        if (getActivity() != null)
            barcodeReader = (BarcodeReader) getActivity().getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);
    }

    @Override
    public void onScanned(Barcode barcode) {
        // playing barcode reader beep sound
        barcodeReader.playBeep();

        // ticket details activity by passing barcode
        Intent returnIntent = new Intent();
        returnIntent.putExtra("code", barcode.displayValue);
        if (getActivity() != null) {
            getActivity().setResult(Activity.RESULT_OK, returnIntent);
            getActivity().finish();
        }

    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {
        Toast.makeText(getContext(), "Error occurred while scanning " + errorMessage, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCameraPermissionDenied() {
        Intent returnIntent = new Intent();
        if (getActivity() != null) {
            getActivity().setResult(Activity.RESULT_CANCELED, returnIntent);
            getActivity().finish();
        }

    }
}
