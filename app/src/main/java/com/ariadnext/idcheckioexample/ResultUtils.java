package com.ariadnext.idcheckioexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ariadnext.android.smartsdk.interfaces.bean.AXTDocument;
import com.ariadnext.android.smartsdk.interfaces.bean.AXTDocumentAbstract;
import com.ariadnext.android.smartsdk.interfaces.bean.AXTDocumentIdentity;
import com.ariadnext.android.smartsdk.interfaces.bean.AXTImageResult;
import com.ariadnext.android.smartsdk.interfaces.bean.AXTSdkResult;
import com.ariadnext.android.smartsdk.utils.AXTStringUtils;
import com.ariadnext.android.smartsdk.utils.ExtIntentUtils;

import butterknife.ButterKnife;


public class ResultUtils {
    public static final ResultUtils INSTANCE = new ResultUtils();

    public void showSnackBarWithMsg(final String msg, final View parentView) {
        Snackbar snack = Snackbar.make(parentView, msg, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
    }

    public void updateResultState(MainActivity mainActivity, AXTSdkResult result) {
        Context context = mainActivity.getApplicationContext();

        ImageView recto = ButterKnife.findById(mainActivity, R.id.recto);
        /*Recto*/
        final AXTImageResult imageCroppedAXT = result.getMapImageCropped().get(AXTSdkResult.IMAGES_RECTO);
        byte[] imageResult = null;
        if(imageCroppedAXT != null) {
            imageResult = ExtIntentUtils.convertImageToByte(context, Uri.parse(imageCroppedAXT.getImageUri()));
            final Bitmap bitmapCropped = BitmapFactory.decodeByteArray(imageResult, 0, imageResult.length);
            recto.setImageBitmap(bitmapCropped);
        }

        ImageView verso = ButterKnife.findById(mainActivity, R.id.verso);
        /*If verso*/
        final AXTImageResult imageCroppedVerso = result.getMapImageCropped().get(AXTSdkResult.IMAGES_VERSO);
        byte[] imageResultVerso = null;
        if (imageCroppedVerso != null) {
            imageResultVerso = ExtIntentUtils.convertImageToByte(context, Uri.parse(imageCroppedVerso.getImageUri()));
            final Bitmap bitmapCroppedVerso = BitmapFactory.decodeByteArray(imageResultVerso, 0, imageResultVerso.length);
            verso.setImageBitmap(bitmapCroppedVerso);
        }


        /*Texte résultat*/
        AXTDocumentIdentity documentIdentity = (AXTDocumentIdentity) result.getMapDocument().get(AXTSdkResult.IDENTITY_DOCUMENT);
        if(documentIdentity != null) {
            TextView numDoc = ButterKnife.findById(mainActivity, R.id.num_document);
            if (AXTStringUtils.isNotEmpty(documentIdentity.getField(AXTDocumentIdentity.AxtField.DOCUMENT_NUMBER)))
                numDoc.setText(documentIdentity.getField(AXTDocumentIdentity.AxtField.DOCUMENT_NUMBER));
    
            TextView mrz = ButterKnife.findById(mainActivity, R.id.mrz);
            if (AXTStringUtils.isNotEmpty(documentIdentity.getField(AXTDocumentAbstract.AxtField.CODELINE)))
                mrz.setText(documentIdentity.getField(AXTDocumentAbstract.AxtField.CODELINE));
    
            TextView naissance = ButterKnife.findById(mainActivity, R.id.naissance);
            if (AXTStringUtils.isNotEmpty(documentIdentity.getField(AXTDocumentIdentity.AxtField.BIRTH_DATE)))
                naissance.setText(documentIdentity.getField(AXTDocumentIdentity.AxtField.BIRTH_DATE));
    
            TextView nom = ButterKnife.findById(mainActivity, R.id.nom);
            if (AXTStringUtils.isNotEmpty(documentIdentity.getField(AXTDocumentIdentity.AxtField.LAST_NAMES))
                    && AXTStringUtils.isNotEmpty(documentIdentity.getField(AXTDocumentIdentity.AxtField.FIRST_NAMES))) {
                String noms = documentIdentity.getField(AXTDocumentIdentity.AxtField.LAST_NAMES).concat(", ");
                String prenoms = documentIdentity.getField(AXTDocumentIdentity.AxtField.FIRST_NAMES);
                String[] prenomArray = prenoms.split(",");
                nom.setText(noms.concat(prenomArray[0]));
            }
    
            TextView expire = ButterKnife.findById(mainActivity, R.id.expire);
            if (AXTStringUtils.isNotEmpty(documentIdentity.getField(AXTDocumentIdentity.AxtField.EXPIRATION_DATE)))
                expire.setText(documentIdentity.getField(AXTDocumentIdentity.AxtField.EXPIRATION_DATE));
        }
    }
}
