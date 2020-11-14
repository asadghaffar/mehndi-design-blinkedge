package com.blinkedge.mehndidesign.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blinkedge.mehndidesign.BuildConfig;
import com.blinkedge.mehndidesign.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FullimageScreen extends AppCompatActivity {

    private LinearLayout shareLinear;
    private MaterialTextView OnBackPressFullImage;
    private ImageView fullimageView;
    private MaterialButton buttonNext;
    private MaterialButton download_only_button;
    private CircleImageView moreShareOption;
    private CircleImageView whatsappImage;
    private CircleImageView messengerImage;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (android.os.Build.VERSION.SDK_INT > 23) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fullimage_screen);

        id();
        OnClick();
        buttonAnimation();

        // Fetching image path from previous activity
        try {
            imagePath = getIntent().getExtras().getString("imagePath", "");
            Log.d("imagePath", imagePath);
            if (!imagePath.equals("")) {
                Picasso.get().load(imagePath).placeholder(R.drawable.loading)
                        .into(fullimageView, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(FullimageScreen.this, "Check Your Internet Connection",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Button Animation
    public void buttonAnimation() {
        ScaleAnimation scal = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
        scal.setDuration(2000);
        scal.setFillAfter(true);
        buttonNext.setAnimation(scal);
    }

    // Onclick listeners
    private void OnClick() {

        OnBackPressFullImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareLinear.setVisibility(View.INVISIBLE);
            }
        });

        messengerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FullimageScreen.this, "In progress", Toast.LENGTH_SHORT).show();
            }
        });

        whatsappImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FullimageScreen.this, "In progress", Toast.LENGTH_SHORT).show();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareLinear.setVisibility(View.VISIBLE);
            }
        });

        download_only_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Dexter.withContext(FullimageScreen.this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (!report.areAllPermissionsGranted()) {
                                Toast.makeText(FullimageScreen.this, "Enable the permissions to download and use the wallpapers!"
                                        , Toast.LENGTH_SHORT).show();
                            } else {
                                saveAction();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
                }
        });

        moreShareOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareItem(imagePath);
            }
        });
    }

   /* private void messengerShareImage(String messengerAppUrl) {
        try {
            Picasso.get().load(messengerAppUrl).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Log.d("messenger", "loaded_" + bitmap);
                    Log.d("messengerUrl", "url=" + imagePath);
                    Intent messengerShareIntent = new Intent(Intent.ACTION_SEND);
                    messengerShareIntent.setType("image/*");
                    messengerShareIntent.putExtra(Intent.EXTRA_STREAM, (getLocalBitmapUri(bitmap)));
                    messengerShareIntent.setPackage("com.facebook.orca");
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    Log.d("failedMessenger", "error=" + e.getMessage());
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    Toast.makeText(FullimageScreen.this, "In progress", Toast.LENGTH_SHORT).show();
                    Log.d("preparedMessenger", "prepare_");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void whatsappShareImage(String whatsappUrl) {
        try {
            Picasso.get().load(whatsappUrl).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Log.d("whatsapp", "loaded_" + bitmap);
                    Log.d("whatsappUrl", "url=" + imagePath);
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("image/*");
                    i.setPackage("com.whatsapp");
                    i.putExtra(Intent.EXTRA_STREAM, (getLocalBitmapUri(bitmap)));
                    try {
                        FullimageScreen.this.startActivity(i);
                    } catch (ActivityNotFoundException ex) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp")));
                    }
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    Log.d("failedWhatsapp", "error=" + e.getMessage());
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    Toast.makeText(FullimageScreen.this, "In Progress", Toast.LENGTH_SHORT).show();
                    Log.d("preparedWhatsapp", "prepare = ");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    // more share option
    public void shareItem(String url/*, final String pak*/) {
        try {
            Picasso.get().load(url).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Log.d("shareItem", "loaded_" + bitmap);
                    Log.d("shareItemUrl", "url=" + imagePath);
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("image/*");
                    String shareMessage = "https://play.google.com/store/apps/details?id=" + FullimageScreen.this.getPackageName() + "\n\n";
                    i.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    i.putExtra(Intent.EXTRA_STREAM, (getLocalBitmapUri(bitmap)));
                    startActivity(Intent.createChooser(i, "Share Image"));
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    Log.d("failedShareItem", "error=" + e.getMessage());
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    Log.d("preparedSharedItem", "prepare_");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM), "IMG_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 60, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    // Saving Image To Internal Storage
    public void saveAction() {
        DownloadManager mgr = (DownloadManager) FullimageScreen.this.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(imagePath);
        Log.d("Sdfs__", downloadUri + "");
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                        getString(R.string.app_name) + System.currentTimeMillis() + ".jpg");
        mgr.enqueue(request);
        Log.d("mgr", String.valueOf(mgr));
        Log.d(String.valueOf(downloadUri), "downladoUri_");
        Toast.makeText(FullimageScreen.this, "Image Downloaded!", Toast.LENGTH_SHORT).show();
    }

    @Override
    // Hide Share Layout on backPress
    public void onBackPressed() {

        if (shareLinear.getVisibility() == View.VISIBLE) {
            shareLinear.setVisibility(View.GONE);
        } else
            super.onBackPressed();

    }

    // Ids Function
    private void id() {
        fullimageView = findViewById(R.id.full_image);
        download_only_button = findViewById(R.id.download_only_button);
        moreShareOption = findViewById(R.id.more_share_option);
        buttonNext = findViewById(R.id.next_button);
        shareLinear = findViewById(R.id.share_linear);
        whatsappImage = findViewById(R.id.whatsapp_image);
        messengerImage = findViewById(R.id.messenger_image);
        OnBackPressFullImage = findViewById(R.id.on_back_press);

    }
}