# sadiephone 1.0.0 doğrulama

2026-10-24 · JDK 21 · Gradle 8.11.1 · SDK 35 · minSdk 26.

- Release APK: R8 kod ve kaynak küçültme ile derlendi; özel RSA-3072 anahtarla imzalı.
- `testDebugUnitTest`: 17 test, 0 hata. Katalog, tasarım kilitleri, üretim/stok/iade,
  fiyat-talep etkisi, borç/faiz, nakit-özkaynak-kâr eşitliği ve JSON kayıt testleri.
- `lintRelease`: hata yok; bağımlılık güncelleme uyarıları mevcut.
- Android 11 x86_64 emülatöründe 3 cihaz testi başarılı: şirket kurma/tasarım/lansman/
  satış/yorum, atomik kayıt yeniden açma/ayrı kayıt alanları ve gerçek MainActivity
  açılışı/ağ izni olmaması.
- Küçültülmüş yayın APK’si ayrıca emülatöre kuruldu; MainActivity açılışı başarılı.
- İmza parmak izi `release-certificate.sha256` dosyasıyla eşleşir. Derleme, başka
  bir dağıtım sertifikası verilirse yayını durdurur.
- UI ekran görüntüleri `docs/screenshots/` içindedir.

Gerçek telefonda FPS/pil ölçümü yapılmadı. Bulut kaydı, çok oyunculu mod ve gerçek
cihazlar arası ilerleme aktarımı yoktur.
