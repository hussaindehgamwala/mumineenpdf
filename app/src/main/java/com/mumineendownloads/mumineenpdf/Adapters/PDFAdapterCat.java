package com.mumineendownloads.mumineenpdf.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.aspsine.multithreaddownload.DownloadManager;
import com.itextpdf.text.pdf.PdfReader;
import com.marcinorlowski.fonty.Fonty;
import com.mumineendownloads.mumineenpdf.Activities.PDFActivity;
import com.mumineendownloads.mumineenpdf.Fragments.Go;
import com.mumineendownloads.mumineenpdf.Fragments.PDFListFragment;
import com.mumineendownloads.mumineenpdf.Helpers.PDFHelper;
import com.mumineendownloads.mumineenpdf.Helpers.Status;
import com.mumineendownloads.mumineenpdf.Helpers.Utils;
import com.mumineendownloads.mumineenpdf.Model.PDF;
import com.mumineendownloads.mumineenpdf.R;
import com.mumineendownloads.mumineenpdf.Service.DownloadService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.mumineendownloads.mumineenpdf.R.id.sectionHeader;

/**
 * Created by Hussain on 7/8/2017.
 */


public class PDFAdapterCat extends BasePDFAdapter {

    private final Context context;
    private ArrayList<PDF.PdfBean> pdfBeanArrayList;
    private PDFListFragment pdfListFragment;
    private PDFHelper pdfHelper;


    public PDFAdapterCat(ArrayList<PDF.PdfBean> itemList, Context context, PDFListFragment pdfListFragment) {
        super(itemList);
        pdfHelper = new PDFHelper(context);
        this.pdfBeanArrayList = itemList;
        this.context = context;
        this.pdfListFragment = pdfListFragment;
    }

    @Override
    public boolean onPlaceSubheaderBetweenItems(int position) {
        final PDF.PdfBean pdf = pdfBeanArrayList.get(position);
        final PDF.PdfBean nextPdf = pdfBeanArrayList.get(position + 1);
        return !pdf.getCat().equals(nextPdf.getCat());
    }

    @Override
    public void onBindItemViewHolder(final PDFViewHolder holder, final int position) {
        final PDF.PdfBean pdf = pdfBeanArrayList.get(position);
        holder.title.setText(pdf.getTitle());

        String al = "";
        final int pdfDownloadStatus = pdf.getStatus();
        if (pdfDownloadStatus == Status.STATUS_LOADING) {
            holder.imageView.setVisibility(View.GONE);
            holder.progressBarDownload.setVisibility(View.GONE);
            holder.button.setVisibility(View.GONE);
            holder.size.setText("Connecting..");
            holder.cancel.setVisibility(View.VISIBLE);
            holder.cancelView.setVisibility(View.VISIBLE);
            holder.loading.setVisibility(View.VISIBLE);
        } else if(pdfDownloadStatus == PDF.STATUS_QUEUED) {
            holder.imageView.setVisibility(View.VISIBLE);
            holder.progressBarDownload.setVisibility(View.GONE);
            holder.button.setVisibility(View.GONE);
            holder.size.setText("Queued..");
            holder.cancel.setVisibility(View.GONE);
            holder.cancelView.setVisibility(View.GONE);
        }else if (pdfDownloadStatus == Status.STATUS_DOWNLOADING) {
            holder.imageView.setVisibility(View.GONE);
            holder.progressBarDownload.setVisibility(View.VISIBLE);
            holder.button.setVisibility(View.GONE);
            holder.loading.setVisibility(View.GONE);
            holder.cancel.setVisibility(View.VISIBLE);
            holder.cancelView.setVisibility(View.VISIBLE);
            holder.progressBarDownload.setProgress(pdf.getProgress());
            holder.size.setText(pdf.getDownloadPerSize());
        } else if (pdfDownloadStatus == Status.STATUS_DOWNLOADED) {
            holder.imageView.setVisibility(View.VISIBLE);
            holder.progressBarDownload.setVisibility(View.GONE);
            holder.size.setText(getPagesString(pdf.getPageCount()) + Utils.fileSize(pdf.getSize())) ;
            holder.button.setVisibility(View.VISIBLE);
            if(pdfListFragment.isMultiSelect){
                holder.button.setAlpha(0.5f);
                holder.button.setEnabled(false);
                holder.cancelView.setEnabled(false);
            }else{
                holder.button.setAlpha(1.0f);
                holder.button.setEnabled(true);
                holder.cancelView.setEnabled(false);
            }
            holder.loading.setVisibility(View.GONE);
            holder.cancel.setVisibility(View.GONE);
            holder.cancelView.setVisibility(View.VISIBLE);
        }else if(pdfDownloadStatus==Status.STATUS_CONNECTED){
            holder.size.setText("Downloading..");
            holder.loading.setVisibility(View.INVISIBLE);
            holder.imageView.setVisibility(View.INVISIBLE);
            holder.cancel.setVisibility(View.VISIBLE);
            holder.cancelView.setVisibility(View.VISIBLE);
            holder.progressBarDownload.setVisibility(View.VISIBLE);
        }
        else {
            holder.cancelView.setVisibility(View.GONE);
            holder.size.setText(Utils.fileSize(pdf.getSize()) + al);
            holder.imageView.setVisibility(View.VISIBLE);
            holder.progressBarDownload.setVisibility(View.GONE);
            holder.button.setVisibility(View.GONE);
            holder.loading.setVisibility(View.GONE);
            holder.cancel.setVisibility(View.GONE);
        }


        if(pdfListFragment.getMultiSelect_list().contains(pdf)){
            holder.imageView.setImageResource(R.drawable.pdf_downloaded_selected);
            holder.parentView.setBackgroundColor(Color.parseColor("#F3F4F3"));
        }else {
            holder.imageView.setImageResource(R.drawable.pdf_downloaded);
            holder.parentView.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        holder.cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pdfDownloadStatus==Status.STATUS_DOWNLOADED) {
                    if (getFilePages(pdf) != 0) {
                        Intent intent = new Intent(pdfListFragment.getActivity(), PDFActivity.class);
                        intent.putExtra("mode", 0);
                        intent.putExtra("pid", pdf.getPid());
                        intent.putExtra("title", pdf.getTitle());
                        pdfListFragment.startActivity(intent);
                    } else {
                        Toasty.error(context, "Invalid file").show();
                        pdf.setStatus(Status.STATUS_NULL);
                        notifyDataSetChanged();
                        pdfHelper.updatePDF(pdf);
                    }
                } else {
                    DownloadManager.getInstance().cancel(String.valueOf(pdf.getPid()));
                }
            }
        });

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getFilePages(pdf)!=0) {
                    Intent intent = new Intent(pdfListFragment.getActivity(), PDFActivity.class);
                    intent.putExtra("mode",0);
                    intent.putExtra("pid", pdf.getPid());
                    intent.putExtra("title", pdf.getTitle());
                    pdfListFragment.startActivity(intent);
                } else {
                    Toasty.error(context,"Invalid file").show();
                    pdf.setStatus(Status.STATUS_NULL);
                    notifyDataSetChanged();
                    pdfHelper.updatePDF(pdf);
                }
            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager.getInstance().cancel(String.valueOf(pdf.getPid()));
            }
        });

        holder.parentView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                pdfListFragment.enableMultiSelect(position, pdf);
                return true;
            }
        });

        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfListFragment.openDialog(holder.parentView.getContext(),position,pdf);
            }
        });


    }

    @Override
    public void onBindSubheaderViewHolder(SubHeaderHolder subheaderHolder, int nextItemPosition) {
        final PDF.PdfBean nextPDF = pdfBeanArrayList.get(nextItemPosition);
        subheaderHolder.mSubHeaderText.setText(nextPDF.getCat());
    }

    private String getPagesString(int filePages) {
        if(filePages==0){
            return "";
        }
        if(filePages>1){
            return filePages + " pages • ";
        }
        return filePages + " page • ";
    }

    public void filter(ArrayList<PDF.PdfBean>newList) {
        pdfBeanArrayList=new ArrayList<>();
        pdfBeanArrayList.addAll(newList);
        notifyDataSetChanged();
    }

    private int getFilePages(PDF.PdfBean pdf){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Mumineen/"+pdf.getPid() + ".pdf");
        int count;
        try {
            PdfReader pdfReader = new PdfReader(String.valueOf(file));
            count = pdfReader.getNumberOfPages();
            return count;
        } catch (IOException ignored) {
            return 0;
        } catch (NoClassDefFoundError ignored){
            return 0;
        }
    }

}
