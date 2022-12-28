# CodeView [![](https://jitpack.io/v/elmurzaev/CodeView.svg)](https://jitpack.io/#elmurzaev/CodeView)
### Code Editor library for Android.

**Some of the features:**
- Handles very huge amount of lines of code.
- Supports dark mode, it will be turned on automatically if your device is using dark theme.
- Wrap lines.
- Read only mode.
- Beautify code.
- Many other useful features that I might have missed to mention here.

Currently supported languages:
- [x] HTML
- [x] CSS
- [x] JavaScript
- [x] XML

## Download

Step 1. Add JitPack repository in your root build.gradle at the end of repositories:

```Gradle
allprojects {
   repositories {
    ...
    maven { url 'https://jitpack.io' }
   }
}
```
Step 2. Add the dependency
```Gradle
dependencies {
  implementation 'com.github.elmurzaev:CodeView:+'
}
```

## How to use

Include in your layout.
```xml
<com.elmurzaev.codeview.CodeView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/code_view"
    app:beautify="true"
    app:mode="htmlmixed"
    app:read_only="false"
    app:wrap_lines="false" />
```
In your Activity.
```java
CodeView codeView = findViewById(R.id.code_view);
codeView.setCode(mExampleHtml);
codeView.setEventListener(new CodeView.EventListener() {
     @Override
     public void onLoaded() {
         // hide progress bar etc.
     }
});
```

## License
MIT License

Copyright (c) 2022 Ramzan Elmurzaev

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
