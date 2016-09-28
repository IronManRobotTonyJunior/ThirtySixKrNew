package com.example.dllo.thirtysixkr.web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.dllo.thirtysixkr.BaseActivity;
import com.example.dllo.thirtysixkr.R;
import com.example.dllo.thirtysixkr.news.FormatTime;
import com.example.dllo.thirtysixkr.tools.url.Kr36Url;
import com.example.dllo.thirtysixkr.tools.webrequest.SendGetRequest;
import com.example.dllo.thirtysixkr.web.richtext.HtmlTextView;

public class WebDetailActivity extends BaseActivity implements View.OnClickListener {

    private HtmlTextView tv;
    private TextView tvName;
    private TextView tvDetail;
    private TextView tvTime;
    private TextView tvTitle;
    private ImageView circleAuthorIcon;
    private String time;
    private String title;
    private LinearLayout llAuthorDetail;
    private PopupWindow popupWindowUp;
    private AuthorDetailBean.DataBean bean;
    private TextView tvArticles;
    private TextView tvView;
    private ImageView ivFirst;
    private ImageView ivSecond;
    private ImageView ivThird;
    private TextView tvTitleFirst;
    private TextView tvTitleSecond;
    private TextView tvTitleThird;
    private PopupWindow popupWindowDown;
    private View llAuthorLastFirst;
    private View llAuthorLastSecond;
    private View llAuthorLastThird;
    private ImageView ivOther;
    private FrameLayout div;
    private ObservableScrollView scrollView;

    @Override
    protected int setLayout() {
//        struct();
        return R.layout.activity_web_detail;
    }

    @Override
    protected void initView() {


        Intent intent = getIntent();

        String urlFeedId = intent.getStringExtra("web");

        String url = Kr36Url.detailWeb(urlFeedId);

        String authorUrl = Kr36Url.authorDetails(urlFeedId);

        time = FormatTime.formatTime(intent.getLongExtra("time", 0));

        title = intent.getStringExtra("title");

        div = bindView(R.id.web_div);

        scrollView = bindView(R.id.scroll_web);

        // 作者简介的横布局 (设点击事件 下弹Dialog)
        llAuthorDetail = bindView(R.id.web_author_details_ll);

        llAuthorDetail.setOnClickListener(this);

        // 名字头像点进作者详情
        tvName = bindView(R.id.web_tv_author_name);

        tvName.setOnClickListener(this);

        tvDetail = bindView(R.id.web_tv_author_detail);

        tvTime = bindView(R.id.web_tv_time);

        tvTitle = bindView(R.id.web_tv_title);

        circleAuthorIcon = bindView(R.id.ci_author_icon);


        tv = bindView(R.id.tv_detail);

//        tv.setMovementMethod(ScrollingMovementMethod.getInstance());
//
//        tv.setMovementMethod(LinkMovementMethod.getInstance());

        SendGetRequest.sendGetRequest(url, WebDetailBean.class, new SendGetRequest.OnResponseListener<WebDetailBean>() {
            @Override
            public void onResponse(WebDetailBean response) {
                tv.setHtmlFromString(response.getData().getContent());
            }

            @Override
            public void onError() {

            }
        });

        SendGetRequest.sendGetRequest(authorUrl, AuthorDetailBean.class, new SendGetRequest.OnResponseListener<AuthorDetailBean>() {
            @Override
            public void onResponse(AuthorDetailBean response) {
                bean = response.getData();
                tvDetail.setText(response.getData().getBrief());
                tvName.setText(response.getData().getName());

                tvTitle.setText(title);

                Glide.with(WebDetailActivity.this).load(response.getData().getAvatar()).asBitmap().centerCrop().into(new BitmapImageViewTarget(circleAuthorIcon) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        circleAuthorIcon.setImageDrawable(circularBitmapDrawable);
                    }
                });

                tvTime.setText(time);
                circleAuthorIcon.setOnClickListener(WebDetailActivity.this);
            }

            @Override
            public void onError() {

            }
        });


    }

    protected void initData() {
        initPopupWindowUp();
        scrollView.setOnScrollChangedCallback(new ObservableScrollView.OnScrollChangedCallback() {
            @Override
            public void onScroll(int dx, int dy) {
                if (dy > 5) {
                    div.setVisibility(View.GONE);
                }
                if (dy < -5) {
                    div.setVisibility(View.VISIBLE);
                }
            }
        });


    }


//    private void initPopupWindowDown() {
//        popupWindowDown = new PopupWindow(this);
//
//        View view = LayoutInflater.from(this).inflate(R.layout.dialog_web_detail, null);
//
//        popupWindowDown.setContentView(view);
//
//        popupWindowDown.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//
//        popupWindowDown.setAnimationStyle(R.style.ActionSheetDialogAnimation);
//        popupWindowDown.showAtLocation(, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0, 0);
//
//
//        ImageView imgBack = (ImageView) view.findViewById(R.id.dialog_web_img_back);
//        ImageView imgComment = (ImageView) view.findViewById(R.id.dialog_web_img_comment);
//        RadioButton radioButton = (RadioButton) view.findViewById(R.id.dialog_web_radio_favorite);
//        ImageView imgShare = (ImageView) view.findViewById(R.id.dialog_web_img_share);
//        ImageView imgMore = (ImageView) view.findViewById(R.id.dialog_web_img_more);
//        imgBack.setOnClickListener(this);
//        imgComment.setOnClickListener(this);
//        radioButton.setOnClickListener(this);
//        imgShare.setOnClickListener(this);
//        imgMore.setOnClickListener(this);
//
////    private class MyWebViewClient extends WebViewClient {
////        @Override
////        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//////            view.loadUrl(url);
////            return true;
////        }
////
////
////
////    }
//
////    @Override
////    public boolean onKeyDown(int keyCode, KeyEvent event) {
////        if ((keyCode == KeyEvent.KEYCODE_BACK) && wb.canGoBack()) {
////            wb.goBack(); //goBack()表示返回WebView的上一页面
////            return true;
////        }
////        return false;
////    }
//    }

    private void initPopupWindowUp() {
        popupWindowUp = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View view = LayoutInflater.from(this).inflate(R.layout.author_popup_window_up, null);
        popupWindowUp.setContentView(view);
        tvArticles = (TextView) view.findViewById(R.id.tv_author_popup_articles);
        tvView = (TextView) view.findViewById(R.id.tv_author_popup_view);
        ivFirst = (ImageView) view.findViewById(R.id.iv_author_news_first);
        ivSecond = (ImageView) view.findViewById(R.id.iv_author_news_second);
        ivThird = (ImageView) view.findViewById(R.id.iv_author_news_third);
        tvTitleFirst = (TextView) view.findViewById(R.id.tv_author_title_first);
        tvTitleSecond = (TextView) view.findViewById(R.id.tv_author_title_second);
        tvTitleThird = (TextView) view.findViewById(R.id.tv_author_title_third);
        llAuthorLastFirst = view.findViewById(R.id.author_last_first);

        llAuthorLastSecond = view.findViewById(R.id.author_last_second);

        llAuthorLastThird = view.findViewById(R.id.author_last_third);

        ivOther = (ImageView) view.findViewById(R.id.tv_author_popup_other);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.web_author_details_ll:
                if (popupWindowUp.isShowing()) {
                    popupWindowUp.dismiss();
                } else {
                    popupWindowUp.setAnimationStyle(R.style.popwin_anim_style);
                    popupWindowUp.showAsDropDown(llAuthorDetail);
                    if (tv.getVisibility() == View.VISIBLE) {
                        tvArticles.setText(bean.getTotalCount() + "篇");
                        tvView.setText(bean.getTotalView() / 10000 + "万");
                        llAuthorLastFirst.setOnClickListener(this);
                        llAuthorLastSecond.setOnClickListener(this);
                        llAuthorLastThird.setOnClickListener(this);
                        ivOther.setOnClickListener(this);
                        switch (bean.getLatestArticle().size()) {
                            case 3:
                                tvTitleThird.setText(bean.getLatestArticle().get(2).getTitle());
                                Glide.with(this).load(bean.getLatestArticle().get(2).getFeatureImg()).into(ivThird);
                            case 2:
                                Glide.with(this).load(bean.getLatestArticle().get(1).getFeatureImg()).into(ivSecond);
                                tvTitleSecond.setText(bean.getLatestArticle().get(1).getTitle());
                                if (bean.getLatestArticle().size() == 2) {
                                    llAuthorLastThird.setVisibility(View.GONE);
                                }
                            case 1:
                                Glide.with(this).load(bean.getLatestArticle().get(0).getFeatureImg()).into(ivFirst);
                                tvTitleFirst.setText(bean.getLatestArticle().get(0).getTitle());
                                if (bean.getLatestArticle().size() == 1) {
                                    llAuthorLastSecond.setVisibility(View.GONE);
                                    llAuthorLastThird.setVisibility(View.GONE);
                                }
                        }
                    }
                }
                break;
            case R.id.web_tv_author_name:
                break;
            case R.id.ci_author_icon:
                break;
            case R.id.tv_author_popup_other:
                popupWindowUp.dismiss();
                Log.d("WebDetailActivity", "进");
                break;
            case R.id.author_last_first:
                if (bean.getLatestArticle().get(0).getTitle().equals(title)) {
                    Toast.makeText(this, "您正在看此篇文章,可以看看其他感兴趣的文章", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.author_last_second:
                if (bean.getLatestArticle().get(1).getTitle().equals(title)) {
                    Toast.makeText(this, "您正在看此篇文章,可以看看其他感兴趣的文章", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.author_last_third:
                if (bean.getLatestArticle().get(2).getTitle().equals(title)) {
                    Toast.makeText(this, "您正在看此篇文章,可以看看其他感兴趣的文章", Toast.LENGTH_SHORT).show();
                }
                break;

        }

    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.activity_out);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupWindowUp.isShowing()){
            popupWindowUp.dismiss();
        }
    }
    //    public static void struct() {
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .detectDiskReads().detectDiskWrites().detectNetwork() // or
//                // .detectAll()
//                // for
//                // all
//                // detectable
//                // problems
//                .penaltyLog().build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
//                .penaltyLog() // 打印logcat
//                .penaltyDeath().build());
//    }


}