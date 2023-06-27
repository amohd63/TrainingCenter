package com.example.trainingcenter.View.Instructor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.trainingcenter.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class ListOfStudents extends AppCompatActivity {
    private FirebaseFirestore db;
    private LinearLayout coursesMainView;
    private LinearLayout row;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liststudent_instructor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = FirebaseFirestore.getInstance();
        coursesMainView = (LinearLayout) findViewById(R.id.courses_main_view);

        // Create the courses_list LinearLayout
        LinearLayout coursesListLayout = new LinearLayout(this);
        LinearLayout.LayoutParams coursesListLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        coursesListLayout.setOrientation(LinearLayout.HORIZONTAL);
        coursesListLayout.setWeightSum(100);
        int marginInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        coursesListLayoutParams.setMargins(coursesListLayoutParams.leftMargin, coursesListLayoutParams.topMargin, coursesListLayoutParams.rightMargin, marginInPixels);
        coursesListLayout.setLayoutParams(coursesListLayoutParams);
        coursesListLayout.setId(View.generateViewId());
        CardView cardView = createCourseCardView("NLP", "08:30 - 10:00", "12 Jul 2023", "Dr. test");
        coursesListLayout.addView(cardView);
        coursesMainView.addView(coursesListLayout);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });


        //works good ------------
//        for (int i = 0; i < 9; i++){
//            CardView cardView = createCourseCardView("Test", "08:00 - 10:00", "12 Jul 2023", "Dr. test");
//            coursesListLayout.addView(cardView);
//            if ((i + 1) % 2 == 0){
//                coursesMainView.addView(coursesListLayout);
//                coursesListLayout = new LinearLayout(this);
//                coursesListLayoutParams = new LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                coursesListLayout.setOrientation(LinearLayout.HORIZONTAL);
//                coursesListLayout.setWeightSum(100);
//                marginInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
//                coursesListLayoutParams.setMargins(coursesListLayoutParams.leftMargin, coursesListLayoutParams.topMargin, coursesListLayoutParams.rightMargin, marginInPixels);
//                coursesListLayout.setLayoutParams(coursesListLayoutParams);
//                coursesListLayout.setId(View.generateViewId());
//            }else if (i == 8){
//                coursesMainView.addView(coursesListLayout);
//            }
//        }

//        CardView cardView = createCourseCardView();
//        CardView cardView2 = createCourseCardView();
//        coursesListLayout.addView(cardView);
//        coursesListLayout.addView(cardView2);
//
//        // Add the courses_list LinearLayout to the LinearLayout
//        coursesMainView.addView(coursesListLayout);

//        // Create the second LinearLayout inside the LinearLayout
//        LinearLayout ongoingSubmissionLayout = new LinearLayout(this);
//        LinearLayout.LayoutParams ongoingSubmissionLayoutParams = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        ongoingSubmissionLayout.setOrientation(LinearLayout.VERTICAL);
//
//        // Create the View separator
//        View separatorView = new View(this);
//        LinearLayout.LayoutParams separatorLayoutParams = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, 1);
//        separatorLayoutParams.setMargins(0, 8, 0, 8);
//        separatorView.setLayoutParams(separatorLayoutParams);
//        separatorView.setBackgroundColor(Color.parseColor("#80D1D1D1"));
//
//        // Create the "Ongoing Submission(5)" TextView
//        TextView ongoingSubmissionTextView = createTextView(this, "Ongoing Submission(5)", 18, Typeface.DEFAULT_BOLD);
//        ongoingSubmissionTextView.setGravity(Gravity.CENTER);
//
//        // Create the HorizontalScrollView
//        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);
//        LinearLayout.LayoutParams horizontalScrollViewParams = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        horizontalScrollView.setHorizontalScrollBarEnabled(false);
//        horizontalScrollView.setLayoutParams(horizontalScrollViewParams);
//
//        // Create the LinearLayout inside the HorizontalScrollView
//        LinearLayout innerHorizontalLinearLayout = new LinearLayout(this);
//        LinearLayout.LayoutParams innerHorizontalLinearLayoutParams = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        innerHorizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
//        innerHorizontalLinearLayout.setPadding(8, 8, 8, 8);
//        innerHorizontalLinearLayout.setLayoutParams(innerHorizontalLinearLayoutParams);
//
//        // Create the CardView inside the LinearLayout
//        CardView innerCardView = new CardView(this);
//        LinearLayout.LayoutParams innerCardViewParams = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        innerCardViewParams.setMargins(0, 0, 4, 0);
//        innerCardView.setLayoutParams(innerCardViewParams);
//        innerCardView.setRadius(4);
//        innerCardView.setUseCompatPadding(true);
//        innerCardView.setContentPadding(8, 8, 8, 8);
//
//        // Create the LinearLayout inside the CardView
//        LinearLayout innerLinearLayout2 = new LinearLayout(this);
//        LinearLayout.LayoutParams innerLinearLayoutParams2 = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        innerLinearLayout2.setOrientation(LinearLayout.VERTICAL);
//        innerLinearLayout2.setLayoutParams(innerLinearLayoutParams2);
//
//        // Create the TextViews inside the LinearLayout
//        TextView titleTextView2 = createTextView(this, "Architecture Design", 16, Typeface.DEFAULT);
//        TextView descriptionTextView2 = createTextView(this, "Create an Architecture design, Case Study:..", 14, Typeface.DEFAULT);
//        LinearLayout.LayoutParams descriptionTextViewParams2 = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        descriptionTextViewParams2.setMargins(0, 0, 0, 8);
//        descriptionTextView2.setLayoutParams(descriptionTextViewParams2);
//
//        // Create the LinearLayout inside the LinearLayout
//        LinearLayout innerLinearLayout3 = new LinearLayout(this);
//        LinearLayout.LayoutParams innerLinearLayoutParams3 = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        innerLinearLayout3.setOrientation(LinearLayout.HORIZONTAL);
//        innerLinearLayout3.setLayoutParams(innerLinearLayoutParams3);
//
//        // Create the TextViews inside the LinearLayout
//        TextView durationTextView = createTextView(this, "3d", 14, Typeface.DEFAULT);
//        durationTextView.setGravity(Gravity.START);
//        durationTextView.setTextColor(Color.parseColor("#1F2C77"));
//        TextView codeTextView = createTextView(this, "AD1", 14, Typeface.DEFAULT);
//        codeTextView.setGravity(Gravity.END);
//
//        // Add the TextViews to the LinearLayout
//        innerLinearLayout3.addView(durationTextView);
//        innerLinearLayout3.addView(codeTextView);
//
//        // Add the TextViews to the LinearLayout
//        innerLinearLayout2.addView(titleTextView2);
//        innerLinearLayout2.addView(descriptionTextView2);
//        innerLinearLayout2.addView(innerLinearLayout3);
//
//        // Add the LinearLayout to the CardView
//        innerCardView.addView(innerLinearLayout2);
//
//        // Add the CardView to the LinearLayout
//        innerHorizontalLinearLayout.addView(innerCardView);
//
//        // Add the LinearLayout to the HorizontalScrollView
//        horizontalScrollView.addView(innerHorizontalLinearLayout);
//
//        // Add the views to the second LinearLayout
//        ongoingSubmissionLayout.addView(separatorView);
//        ongoingSubmissionLayout.addView(ongoingSubmissionTextView);
//        ongoingSubmissionLayout.addView(horizontalScrollView);
//
//        // Add the second LinearLayout to the root LinearLayout
//        linearLayout.addView(ongoingSubmissionLayout);
//
//        // Add the ScrollView to the root RelativeLayout
//        rootView.addView(scrollView);
//
//        // Set the root RelativeLayout as the activity's content view
//        setContentView(rootView);
    }

    private CardView createCourseCardView(String courseName, String time, String date, String instructor) {
        // Create the CardView inside the courses_list LinearLayout
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 50);
        cardViewParams.setMargins(4, 0, 0, 0);
        cardView.setLayoutParams(cardViewParams);
        cardView.setRadius(16);
        cardView.setUseCompatPadding(true);
        //cardView.setContentPadding(8, 8, 8, 8);
        cardView.setContentPadding(16, 32, 16, 32);

        // Create the LinearLayout inside the CardView
        LinearLayout innerLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams innerLinearLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        innerLinearLayout.setOrientation(LinearLayout.VERTICAL);
        innerLinearLayout.setLayoutParams(innerLinearLayoutParams);

        // Create the TextViews inside the LinearLayout
        TextView titleTextView = createTextView(this, courseName, 18, Typeface.DEFAULT);
        titleTextView.setTextColor(Color.parseColor("#000000"));
        titleTextView.setPadding(0, 0, 0, 16);

        TextView timeTextView = createTextView(this, time, 16, Typeface.DEFAULT);
        timeTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_access_time_red_24dp, 0, 0, 0);
        timeTextView.setCompoundDrawablePadding(16);
        timeTextView.setPadding(0, 0, 0, 16);

        TextView dateTextView = createTextView(this, date, 16, Typeface.DEFAULT);
        dateTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_event_available_red_24dp, 0, 0, 0);
        dateTextView.setCompoundDrawablePadding(16);
        dateTextView.setPadding(0, 0, 0, 16);

        TextView instructorTextView = createTextView(this, instructor, 16, Typeface.DEFAULT);
        instructorTextView.setGravity(Gravity.END);

        // Add the TextViews to the LinearLayout
        innerLinearLayout.addView(titleTextView);
        innerLinearLayout.addView(timeTextView);
        innerLinearLayout.addView(dateTextView);
        innerLinearLayout.addView(instructorTextView);

        // Add the LinearLayout to the CardView
        cardView.addView(innerLinearLayout);
        return cardView;
    }

//    private CardView createAcceptedCardView() {
//
//    }

    private TextView createTextView(Context context, String text, int textSize, Typeface typeface) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText(text);
        //textView.setTextColor(Color.parseColor("#000000"));
        textView.setTextSize(textSize);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/calibri.ttf");
        textView.setTypeface(typeface);
        //textView.setFontFamily(getResources().getFont(R.font.calibri));
        return textView;
    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_courses);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        db = FirebaseFirestore.getInstance();
//        coursesList = (LinearLayout) findViewById(R.id.courses_list);
//        //coursesList.removeAllViews();
//        setContentView(R.layout.activity_announcements);
//
//
////        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
////        cardParams.weight = 50;
////        cardParams.rightMargin = 4; // in pixels
////
////        CardView cardView = new CardView(this);
////        cardView.setLayoutParams(cardParams);
////        cardView.setRadius(4); // in pixels
////        cardView.setUseCompatPadding(true);
////        cardView.setContentPadding(8, 8, 8, 8); // in pixels
////
////        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
////                LinearLayout.LayoutParams.MATCH_PARENT,
////                LinearLayout.LayoutParams.WRAP_CONTENT
////        );
////
////        LinearLayout linearLayout = new LinearLayout(this);
////        linearLayout.setLayoutParams(linearParams);
////        linearLayout.setOrientation(LinearLayout.VERTICAL);
////
////        TextView textView1 = new TextView(this);
////        textView1.setLayoutParams(new LinearLayout.LayoutParams(
////                LinearLayout.LayoutParams.MATCH_PARENT,
////                LinearLayout.LayoutParams.WRAP_CONTENT
////        ));
////        textView1.setPadding(0, 0, 0, 2); // in pixels
////        textView1.setTypeface(null, Typeface.BOLD);
////        textView1.setTextColor(0xFF000000); // black color
////        textView1.setTextSize(16); // in scaled pixels
////        textView1.setText("Basic Programming(BC)");
////
////        LinearLayout.LayoutParams subLinearParams = new LinearLayout.LayoutParams(
////                LinearLayout.LayoutParams.MATCH_PARENT,
////                LinearLayout.LayoutParams.MATCH_PARENT
////        );
////        subLinearParams.weight = 50;
////
////        LinearLayout subLinearLayout = new LinearLayout(this);
////        subLinearLayout.setLayoutParams(subLinearParams);
////        subLinearLayout.setOrientation(LinearLayout.VERTICAL);
////
////        TextView textView2 = new TextView(this);
////        textView2.setLayoutParams(new LinearLayout.LayoutParams(
////                LinearLayout.LayoutParams.WRAP_CONTENT,
////                LinearLayout.LayoutParams.WRAP_CONTENT
////        ));
////        textView2.setPadding(0, 0, 0, 4); // in pixels
////        textView2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_access_time_red_24dp, 0, 0, 0);
////        textView2.setCompoundDrawablePadding(8); // in pixels
////        textView2.setGravity(Gravity.CENTER);
////        textView2.setText("08:00 - 10:00");
////
////        TextView textView3 = new TextView(this);
////        textView3.setLayoutParams(new LinearLayout.LayoutParams(
////                LinearLayout.LayoutParams.WRAP_CONTENT,
////                LinearLayout.LayoutParams.WRAP_CONTENT
////        ));
////        textView3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_event_available_red_24dp, 0, 0, 0);
////        textView3.setCompoundDrawablePadding(8); // in pixels
////        textView3.setGravity(Gravity.CENTER);
////        textView3.setText("12 Jul 2019");
////
////        TextView textView4 = new TextView(this);
////        //textView4.setId(R.id.textView);
////        textView4.setLayoutParams(new LinearLayout.LayoutParams(
////                LinearLayout.LayoutParams.MATCH_PARENT,
////                LinearLayout.LayoutParams.WRAP_CONTENT
////        ));
////        textView4.setPadding(0, 0, 0, 4); // in pixels
////        textView4.setText("Dr. Felix");
////        textView4.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
////        textView4.setTextSize(14); // in scaled pixels
////
////        subLinearLayout.addView(textView2);
////        subLinearLayout.addView(textView3);
////
////        linearLayout.addView(textView1);
////        linearLayout.addView(subLinearLayout);
////        linearLayout.addView(textView4);
////
////        cardView.addView(linearLayout);
////
////        //setContentView(cardView);
////        coursesList.addView(cardView);
//
//
////        String[] documentData = new String[2];
////        CollectionReference docRef = db.collection("Course");
////        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
////            @Override
////            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
////                if (task.isSuccessful()) {
////                    DocumentSnapshot document = task.getResult();
////                    if (document.exists()) {
////                        documentData[0] = document.getString("personalPhoto");
////                        documentData[1] = document.getString("firstName") + " " + document.getString("lastName");
////                    } else {
////                        Picasso.get().load(imgUrl).into(profileImg);
////                    }
////                } else {
////                }
////            }
////        });
//
//    }
    public void openDialog() {
        CustomDialog exampleDialog = new CustomDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    private CardView createCardView(Context context, String title, String subTitle) {
        // Create the CardView
        CardView cardView = new CardView(context);
        cardView.setLayoutParams(new CardView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        cardView.setCardBackgroundColor(Color.WHITE);
        cardView.setCardElevation(4);

        // Create the LinearLayout as the CardView's child
        LinearLayout containerLayout = new LinearLayout(context);
        containerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        containerLayout.setOrientation(LinearLayout.VERTICAL);
        containerLayout.setPadding(16, 16, 16, 16);
        // Create the TextView for the title
        TextView titleTextView = new TextView(context);
        titleTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        titleTextView.setText(title);
        titleTextView.setTextColor(Color.BLACK);
        titleTextView.setTextSize(16);

        // Create the TextView for the subtitle
        TextView subTitleTextView = new TextView(context);
        subTitleTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        subTitleTextView.setText(subTitle);
        subTitleTextView.setTextColor(Color.GRAY);
        subTitleTextView.setTextSize(14);

        // Add the TextViews to the containerLayout
        containerLayout.addView(titleTextView);
        containerLayout.addView(subTitleTextView);

        // Add the containerLayout to the CardView
        cardView.addView(containerLayout);

        return cardView;
    }


}
