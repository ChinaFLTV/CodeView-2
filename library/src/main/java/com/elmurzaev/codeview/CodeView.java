/*
 * MIT License
 *
 * Copyright (c) 2022 Ramzan Elmurzaev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.elmurzaev.codeview;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

public class CodeView extends WebView {

    @NonNull
    private String mCode = "";
    @NonNull
    private String mMode = "htmlmixed";
    private boolean mBeautify = true;
    private boolean mReadOnly = false;
    private boolean mWrapLines = false;
    @Nullable
    private EventListener mEventListener;
    @Nullable
    private ValueCallback<String> mValueCallback;

    public CodeView(@NonNull Context context) {
        this(context, null);
    }

    public CodeView(@NonNull final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        if (attrs != null) {
            TypedArray array = context.getTheme().obtainStyledAttributes(
                    attrs, R.styleable.CodeView, 0, 0);
            mMode = array.getString(R.styleable.CodeView_mode);
            mBeautify = array.getBoolean(R.styleable.CodeView_beautify, mBeautify);
            mReadOnly = array.getBoolean(R.styleable.CodeView_read_only, mReadOnly);
            mWrapLines = array.getBoolean(R.styleable.CodeView_wrap_lines, mWrapLines);
            array.recycle();
        }
        if (isDarkMode()) {
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                WebSettingsCompat.setForceDark(getSettings(), WebSettingsCompat.FORCE_DARK_ON);
            }
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK_STRATEGY)) {
                WebSettingsCompat.setForceDarkStrategy(getSettings(),
                        WebSettingsCompat.DARK_STRATEGY_PREFER_WEB_THEME_OVER_USER_AGENT_DARKENING);
            }
        }
        setOnTouchListener(new View.OnTouchListener() {
            private final ScaleGestureDetector mDetector =
                    new ScaleGestureDetector(context, new GestureListener());

            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                mDetector.onTouchEvent(motionEvent);
                return false;
            }
        });
        setWebViewClient(new WebClient());
        getSettings().setJavaScriptEnabled(true);
        addJavascriptInterface(new JsInterface(), "Client");
        loadUrl("file:///android_asset/codeview/editor.html");
    }

    public int getFontSize() {
        return getSettings().getDefaultFixedFontSize();
    }

    public void setFontSize(int fontSize) {
        getSettings().setDefaultFixedFontSize(fontSize);
    }

    public void undo() {
        evaluateJavascript("editor.execCommand('undo')", null);
    }

    public void redo() {
        evaluateJavascript("editor.execCommand('redo')", null);
    }

    public void find() {
        evaluateJavascript("editor.execCommand('findPersistent')", null);
    }

    @NonNull
    public String getMode() {
        return mMode;
    }

    public void setMode(@NonNull String mode) {
        mMode = mode;
    }

    public void setBeautify(boolean beautify) {
        mBeautify = beautify;
    }

    public boolean isReadOnly() {
        return mReadOnly;
    }

    public void setReadOnly(boolean readOnly) {
        mReadOnly = readOnly;
        evaluateJavascript("editor.setOption('readOnly', " +
                "Client.isReadOnly())", null);
    }

    public void setWrapLines(boolean wrapLines) {
        mWrapLines = wrapLines;
        evaluateJavascript("editor.setOption('lineWrapping', " +
                "Client.shouldWrapLines())", null);
    }

    public void setEventListener(@Nullable EventListener eventListener) {
        mEventListener = eventListener;
    }

    public boolean isDarkMode() {
        int mask = getContext().getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        return mask == Configuration.UI_MODE_NIGHT_YES;
    }

    public void setCode(@NonNull String code) {
        mCode = code;
        evaluateJavascript("editor.setValue(Client.getCode())", null);
    }

    public void getCode(ValueCallback<String> callback) {
        mValueCallback = callback;
        evaluateJavascript("Client.onReceiveResult(editor.getValue())", null);
    }

    private class JsInterface {

        @JavascriptInterface
        public void onReceiveResult(final String result) {
            post(new Runnable() {
                @Override
                public void run() {
                    if (mValueCallback != null) {
                        mValueCallback.onReceiveValue(result);
                    }
                }
            });
        }

        @JavascriptInterface
        public String getCode() {
            return mCode;
        }

        @JavascriptInterface
        public String getMode() {
            return mMode;
        }

        @JavascriptInterface
        public boolean isReadOnly() {
            return mReadOnly;
        }

        @JavascriptInterface
        public boolean shouldBeautify() {
            return mBeautify;
        }

        @JavascriptInterface
        public boolean shouldWrapLines() {
            return mWrapLines;
        }

        @JavascriptInterface
        public void onLoaded() {
            if (mEventListener != null) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        mEventListener.onLoaded();
                    }
                });
            }
        }

    }

    private static final class WebClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.requestFocus();
        }

    }

    public interface EventListener {

        void onLoaded();

    }

    private class GestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        int mScale = 100;

        @Override
        public boolean onScale(@NonNull ScaleGestureDetector detector) {
            mScale += detector.getCurrentSpan() > detector.getPreviousSpan() ? 1 : -1;
            mScale = Math.max(70, Math.min(mScale, 130));

            if (getSettings().getTextZoom() != mScale) {
                getSettings().setTextZoom(mScale);
            }
            return super.onScale(detector);
        }

    }

}
