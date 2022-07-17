package com.example.editNote;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DataBase.Notes;
import com.example.myNote.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class EditNoteActivity extends AppCompatActivity implements DeleteDialog.DeleteNoteToEditNoteActivityCallback {
    private EditText editTextHeader;
    private EditText editTextDescription;
    private TextView textViewDate;
    private String id;
    private String noteId;
    private String color;
    private BottomAppBar bottomAppBar;
    private Notes newNote;
    private EditViewModel editViewModel;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private BottomSheetBehavior mBottomSheetBehavior;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int SELECT_PICTURE = 1;
    private String currentPhotoPath;

    private List<Uri> uriPictures = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private RecyclerView recyclerViewImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        editTextHeader = findViewById(R.id.editTextHeader);
        editTextDescription = findViewById(R.id.editTextDescription);
        textViewDate = findViewById(R.id.lastChangeOfDateTextView);
        id = "";
        Log.i("start", "onCreateEditActivity");

        recyclerViewImage = findViewById(R.id.rvListOfPictures);
        recyclerViewImage.setLayoutManager(new GridLayoutManager(this, 3));
        imageAdapter = new ImageAdapter(uriPictures);
        recyclerViewImage.setAdapter(imageAdapter);


        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusBarColor));
        }

        initToolbar();

        bottomAppBar = findViewById(R.id.bottomAppBar);
        initialBottomAppBar();


        FloatingActionButton floatingActionButtonSaveNote = findViewById(R.id.floatingActionButtonSaveNote);
        floatingActionButtonSaveNote.setOnClickListener(onClickSaveNewNote);

        checkIntentFromMainActivity();


        EditViewModelFactory editViewModelFactory = new EditViewModelFactory(noteId, this.getApplication());
        editViewModel = new ViewModelProvider(this, editViewModelFactory).get(EditViewModel.class);

        getIntentFromMainActivity();
    }

    public void checkIntentFromMainActivity() {
        Intent intentGetIdNote = getIntent();
        if (intentGetIdNote.hasExtra("id") && !intentGetIdNote.getStringExtra("id").isEmpty()) {
            noteId = intentGetIdNote.getStringExtra("id");
            Log.i("id", noteId);
        }
    }

    public void getIntentFromMainActivity() {
        if (noteId != null) {
            compositeDisposable.add(editViewModel.getNote()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Notes>() {
                        @Override
                        public void accept(Notes notes) throws Exception {
                            Log.i("id", "one");
                            editTextHeader.setText(notes.getHeader());
                            editTextDescription.setText(notes.getDescription());
                            textViewDate.setVisibility(View.VISIBLE);
                            textViewDate.setText(notes.getDate());
                            id = notes.getId();
                            color = notes.getColor();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable notes) throws Exception {
                            Log.e("error", "error", notes);
                        }
                    }));
        }
    }

    public void initToolbar() {
        Toolbar toolbarEditNote = findViewById(R.id.toolbarEditNoteUp);
        setSupportActionBar(toolbarEditNote);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbarEditNote.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbarEditNote.setNavigationOnClickListener(onClickButtonBack);
    }

    View.OnClickListener onClickButtonBack = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            saveNoteAndFinish();
        }
    };

    View.OnClickListener onClickSaveNewNote = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            saveNoteAndFinish();
        }
    };

    public void saveNoteAndFinish() {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        String date = dateFormat.format(currentDate);
        String header = editTextHeader.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        if (!id.isEmpty()) {
            if (!header.isEmpty() || !description.isEmpty()) {
                newNote = new Notes(id, header, description, color, date);
                insertOrUpdateNote();
            } else {
                deleteNote();
            }
        } else {
            if (!header.isEmpty() || !description.isEmpty()) {
                color = getRandomColor().trim();
                id = UUID.randomUUID().toString().replace("-", "").trim();
                newNote = new Notes(id, header, description, color, date);
                insertOrUpdateNote();
            }
        }
        finish();
    }

    private String getRandomColor() {
        String firstColor = "#E2F3F0";
        String secondColor = "#FDCCCA";
        String thirdColor = "#C3D9FF";
        String fourthColor = "#F8D9DE";
        String fifthColor = "#FFF5E6";
        String[] colors = {firstColor, secondColor, thirdColor, fourthColor, fifthColor};
        String randomColor = colors[new Random().nextInt(colors.length)];
        Log.i("random", "" + randomColor);
        return randomColor;
    }

    public void shareNote() {
        if (editTextHeader.getText().toString().isEmpty() && editTextDescription.getText().toString().isEmpty()) {
            Log.i("dialog", "LinesAreEmpty!");
        } else {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, editTextHeader.getText().toString().trim());
            sendIntent.putExtra(Intent.EXTRA_TEXT, editTextDescription.getText().toString().trim());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
    }

    private void initialBottomAppBar() {
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.toolbarDownButtonDelete:
                        if (id.isEmpty()) {
                            editTextHeader.setText("");
                            editTextDescription.setText("");
                        } else {
                            DialogFragment deleteDialogFragment = new DeleteDialog();
                            deleteDialogFragment.show(getSupportFragmentManager(), "delete");
                        }
                        return true;
                    case R.id.toolbarDownButtonShare:
                        shareNote();
                        return true;
                    case R.id.toolbarDownButtonAdd:
                        openBottomSheetAndChoosePictures();
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    public void insertOrUpdateNote() {
        compositeDisposable.add(editViewModel.insertOrUpdateNote(newNote)
                .subscribeOn(Schedulers.io())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("error", "error", throwable);
                    }
                }));
    }

    public void deleteNote() {
        compositeDisposable.add(editViewModel.deleteNoteById(id)
                .subscribeOn(Schedulers.io())
                .subscribe(new Action() {
                               @Override
                               public void run() throws Exception {

                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("error", "error", throwable);
                               }
                           }
                ));
    }

    public void openBottomSheetAndChoosePictures() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                EditNoteActivity.this, R.style.Theme_MaterialComponents_Light_BottomSheetDialog);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_add_pictures,
                (LinearLayout) findViewById(R.id.modalBottomSheetContainer));
        bottomSheetView.findViewById(R.id.tv_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditNoteActivity.this, "Камера", Toast.LENGTH_LONG).show();
                dispatchTakePictureIntent();
                setLastPhotoToArrayList();
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetView.findViewById(R.id.tv_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditNoteActivity.this, "Галерея", Toast.LENGTH_LONG).show();

                chooseImageFromGallery();
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(EditNoteActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

//    private void galleryAddPic() {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(currentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        Toast.makeText(EditNoteActivity.this, "URI = " + currentPhotoPath, Toast.LENGTH_SHORT).show();
        return image;


    }

    private void chooseImageFromGallery() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE) {
            if (data != null) {
                Uri selectedImageUri = data.getData();


                if (selectedImageUri != null) {
                    imageAdapter.setImages(selectedImageUri);
                }

                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Toast.makeText(EditNoteActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


private void setLastPhotoToArrayList() {
        File f = new File(currentPhotoPath);
        Uri uri = Uri.fromFile(f);
        imageAdapter.setImages(uri);
}

    @Override
    public void onDeleteButtonClicked() {
        deleteNote();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("start", "onStartEditActivity");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.i("start", "onPostResumeEditActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("start", "onPauseEditActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
        Log.i("start", "onStopEditActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("start", "onDestroyEditActivity");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("start", "onBackPressedEditActivity");

    }
}

