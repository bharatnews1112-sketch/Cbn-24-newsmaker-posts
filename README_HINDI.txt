CBN 24 NEWS POST MAKER - FIXED v3

सुधार:
1. पोस्ट का masthead सही अनुपात में दिखेगा; नीचे का अतिरिक्त app-name हिस्सा crop किया गया है।
2. चुनी हुई फोटो पोस्ट में आएगी और फोटो crop होकर तय जगह में फिट होगी।
3. पोस्ट 1080 x 1920 आकार में बनेगी।
4. Headline, पूरी खबर, फोटो, संवाददाता, स्थान और वेबसाइट शामिल होंगे।
5. खबर को उपलब्ध जगह में फिट करने के लिए फ़ॉन्ट आकार घटेगा; फिर भी फिट न होने पर ऐप खबर छोटी करने को कहेगी, चुपचाप टेक्स्ट नहीं काटेगी।
6. पोस्ट Pictures/CBN24 में सेव होगी; समर्थित Android संस्करणों पर शेयर शीट भी खुलेगी।

बिल्ड:
- Android Studio में cbn24_fixed फ़ोल्डर Open करें।
- Gradle Sync होने दें।
- Build > Build APK(s) चुनें।

नोट: यह source project ZIP है, तैयार installable APK नहीं। इस वातावरण में Android SDK/build tools उपलब्ध न होने के कारण APK compile करके सत्यापित नहीं की गई है।

GitHub से APK बनाने का आसान तरीका (नया workflow):
1. ZIP खोलकर cbn24_fixed फ़ोल्डर के अंदर की सभी फ़ाइलें GitHub repository में upload करें ( .github फ़ोल्डर सहित)।
2. GitHub में Actions टैब खोलें और “Build CBN24 APK” workflow चलाएँ; या main/master में push करें।
3. Workflow पूरा होने पर run के नीचे Artifacts में “CBN24-News-Post-Maker-debug-apk” डाउनलोड करें।
4. ZIP artifact खोलकर app-debug.apk निकालें और फोन में install करें। फोन में “Install unknown apps” की अनुमति माँगी जा सकती है।
यदि build फिर भी fail हो, Actions run का लाल error/log भेजें—उसी के आधार पर अगला सुधार करना होगा।
