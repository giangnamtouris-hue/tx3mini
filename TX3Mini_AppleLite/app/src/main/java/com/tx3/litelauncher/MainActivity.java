package com.tx3.litelauncher;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;

public class MainActivity extends Activity {
    private LinearLayout tiles;

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private GradientDrawable rounded(int color, int radius, int strokeColor, int strokeWidth) {
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(color);
        shape.setCornerRadius(dp(radius));
        if (strokeWidth > 0) shape.setStroke(dp(strokeWidth), strokeColor);
        return shape;
    }

    private TextView text(String value, int sizeSp, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextColor(color);
        t.setTextSize(sizeSp);
        if (bold) t.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
        return t;
    }

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        FrameLayout root = new FrameLayout(this);
        ImageView wallpaper = new ImageView(this);
        wallpaper.setImageResource(R.drawable.wallpaper);
        wallpaper.setScaleType(ImageView.ScaleType.CENTER_CROP);
        root.addView(wallpaper, new FrameLayout.LayoutParams(-1, -1));
        View tint = new View(this);
        tint.setBackgroundColor(Color.argb(45, 1, 8, 19));
        root.addView(tint, new FrameLayout.LayoutParams(-1, -1));

        // All measurements are in dp so the layout scales across 720p/1080p TVs.
        LinearLayout column = new LinearLayout(this);
        column.setOrientation(LinearLayout.VERTICAL);
        column.setPadding(dp(56), dp(34), dp(56), dp(42));
        FrameLayout.LayoutParams contentParams = new FrameLayout.LayoutParams(-1, -1);
        root.addView(column, contentParams);

        TextView brand = text("T X 3   •   L I T E", 13, 0xffcad9eb, true);
        column.addView(brand);

        LinearLayout intro = new LinearLayout(this);
        intro.setOrientation(LinearLayout.VERTICAL);
        intro.setGravity(Gravity.CENTER_VERTICAL);
        column.addView(intro, new LinearLayout.LayoutParams(-1, 0, 1));

        TextClock clock = new TextClock(this);
        clock.setFormat24Hour("HH:mm");
        clock.setFormat12Hour("hh:mm");
        clock.setTextSize(76);
        clock.setTextColor(Color.WHITE);
        clock.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        intro.addView(clock);

        TextClock date = new TextClock(this);
        // Vietnamese date; TextClock refreshes itself after date/time changes.
        date.setFormat24Hour("EEEE, 'ngày' dd/MM/yyyy");
        date.setFormat12Hour("EEEE, 'ngày' dd/MM/yyyy");
        date.setTextSize(20);
        date.setTextColor(0xffd1e2f3);
        intro.addView(date);

        TextView appsTitle = text("ỨNG DỤNG CỦA BẠN", 13, 0xffc5d6e8, true);
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(-1, -2);
        labelParams.bottomMargin = dp(18);
        column.addView(appsTitle, labelParams);

        tiles = new LinearLayout(this);
        tiles.setOrientation(LinearLayout.HORIZONTAL);
        column.addView(tiles, new LinearLayout.LayoutParams(-1, dp(164)));

        addTile("▶", "YouTube", 0xffd82935, () -> openYouTube());
        addTile("◎", "Trình duyệt", 0xff3484cd, () -> openBrowser());

        TextView hint = text("◀  ▶  Di chuyển       OK  Mở ứng dụng", 12, 0xffb2c7de, false);
        LinearLayout.LayoutParams hintParams = new LinearLayout.LayoutParams(-1, -2);
        hintParams.topMargin = dp(24);
        column.addView(hint, hintParams);

        TextView settings = text("⚙  Cài đặt thiết bị   ·   MENU trên remote", 12, 0xffb2c7de, false);
        settings.setFocusable(true);
        settings.setClickable(true);
        settings.setPadding(dp(8), dp(8), dp(8), dp(8));
        settings.setOnClickListener(v -> openSettings());
        column.addView(settings);

        setContentView(root);
        tiles.getChildAt(0).requestFocus();
    }

    private void addTile(String icon, String label, int accent, Runnable action) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setFocusable(true);
        card.setClickable(true);
        card.setBackground(rounded(0xb71a3048, 24, 0x6688a7c8, 1));
        card.setElevation(dp(5));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, -1, 1);
        if (tiles.getChildCount() > 0) params.leftMargin = dp(20);
        tiles.addView(card, params);

        TextView symbol = text(icon, 49, accent, true);
        symbol.setGravity(Gravity.CENTER);
        card.addView(symbol);
        TextView caption = text(label, 22, Color.WHITE, true);
        caption.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams captionParams = new LinearLayout.LayoutParams(-1, -2);
        captionParams.topMargin = dp(8);
        card.addView(caption, captionParams);

        card.setOnClickListener(v -> action.run());
        card.setOnFocusChangeListener((v, focused) -> {
            card.animate().scaleX(focused ? 1.035f : 1f)
                    .scaleY(focused ? 1.035f : 1f).setDuration(130).start();
            card.setBackground(rounded(focused ? 0xf12e4e71 : 0xb71a3048, 24,
                    focused ? Color.WHITE : 0x6688a7c8, focused ? 3 : 1));
        });
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            openSettings();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void openSettings() {
        try { startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS)); }
        catch (Exception e) {
            Toast.makeText(this, "Không mở được phần Cài đặt.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean openPackage(String name) {
        Intent i = getPackageManager().getLaunchIntentForPackage(name);
        if (i == null) return false;
        try { startActivity(i); return true; }
        catch (Exception ignored) { return false; }
    }

    private void openYouTube() {
        if (openPackage("com.google.android.youtube.tv") ||
                openPackage("com.google.android.youtube")) return;
        openUrl("https://www.youtube.com/");
    }

    private void openBrowser() { openUrl("https://www.google.com"); }

    private void openUrl(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        i.addCategory(Intent.CATEGORY_BROWSABLE);
        try { startActivity(i); }
        catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Chưa có trình duyệt. Hãy cài trình duyệt trước.",
                    Toast.LENGTH_LONG).show();
        }
    }
}
