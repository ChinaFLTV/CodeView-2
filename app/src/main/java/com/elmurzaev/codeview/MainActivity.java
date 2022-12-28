package com.elmurzaev.codeview;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CodeView codeView = findViewById(R.id.code_view);
        codeView.setCode(mExampleHtml);
        codeView.setEventListener(new CodeView.EventListener() {
            @Override
            public void onLoaded() {
                // hide progress bar etc.
            }
        });
    }

    String mExampleHtml = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "\n" +
            "<head>\n" +
            "    <title>CodeView</title>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta content=\"dark light\" name=\"color-scheme\"/>\n" +
            "    <link href=\"lib/codemirror.css\" rel=\"stylesheet\">\n" +
            "    <link href=\"addon/dialog/dialog.css\" rel=\"stylesheet\">\n" +
            "    <link href=\"addon/simplescrollbars.css\" rel=\"stylesheet\">\n" +
            "    <link href=\"theme/base16-dark.css\" rel=\"stylesheet\">\n" +
            "    <script src=\"lib/codemirror.js\"></script>\n" +
            "    <script src=\"mode/htmlmixed.js\"></script>\n" +
            "    <script src=\"mode/css.js\"></script>\n" +
            "    <script src=\"mode/javascript.js\"></script>\n" +
            "    <script src=\"mode/xml.js\"></script>\n" +
            "    <script src=\"addon/active-line.js\"></script>\n" +
            "    <script src=\"addon/search/search.js\"></script>\n" +
            "    <script src=\"addon/search/searchcursor.js\"></script>\n" +
            "    <script src=\"addon/search/jump-to-line.js\"></script>\n" +
            "    <script src=\"addon/dialog/dialog.js\"></script>\n" +
            "    <script src=\"addon/simplescrollbars.js\"></script>\n" +
            "    <script src=\"lib/beautify.min.js\"></script>\n" +
            "    <script src=\"lib/beautify-css.min.js\"></script>\n" +
            "    <script src=\"lib/beautify-html.min.js\"></script>\n" +
            "    <style>\n" +
            "        body {\n" +
            "            margin: 0 0 0 0;\n" +
            "            line-height: 100%;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "<script>\n" +
            "        editor = CodeMirror(document.body, {\n" +
            "            mode: Client.getMode(),\n" +
            "            value: Client.shouldBeautify() ? html_beautify(Client.getCode()) : Client.getCode(),\n" +
            "            readOnly: Client.isReadOnly(),\n" +
            "            lineWrapping: Client.shouldWrapLines(),\n" +
            "            lineNumbers: true,\n" +
            "            fixedGutter: false,\n" +
            "            styleActiveLine: true,\n" +
            "            spellcheck: false,\n" +
            "            scrollbarStyle: \"overlay\",\n" +
            "            autocorrect: false,\n" +
            "            autocapitalize: false,\n" +
            "        })\n" +
            "        editor.setSize(\"100vw\", \"100vh\")\n" +
            "        editor.refresh = Client.onLoaded()\n" +
            "\n" +
            "        if (window.matchMedia(\"(prefers-color-scheme: dark)\").matches) {\n" +
            "            editor.setOption(\"theme\", \"base16-dark\")\n" +
            "        }\n" +
            "</script>\n" +
            "</body>\n" +
            "</html>";
}