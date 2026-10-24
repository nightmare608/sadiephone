# Yayın imzası

Yayımlanan sadiephone APK’si özel RSA-3072 dağıtım anahtarı kullanır. Keystore,
parola ve yedekleri bu public depoda tutulmaz. Gelecekteki güncellemelerde
aynı anahtar kullanılmalıdır; anahtar kaybolursa eski kuruluma güncelleme yapılamaz.

Yayın akışı:
1. Dağıtım anahtarını ve parola dosyasını özel yerel konumdan seç.
2. `SADIEPHONE_KEYSTORE` ve `SADIEPHONE_PASSWORD_FILE` ortam değişkenlerini ayarla.
3. Artan versionCode ile `assembleRelease` çalıştır.
4. `apksigner verify --print-certs` çıktısındaki SHA-256 değerini
   `release-certificate.sha256` ile karşılaştır. Eşleşmiyorsa yayımlama.
5. Test edilmiş APK ve anahtar içermeyen kaynak ZIP’ini GitHub Release’a ekle.

GitHub Actions test ve lint çalıştırır; geçici debug anahtarıyla APK yayımlamaz.
Anahtarı herkese açık dosyalara veya Actions cache/artifact içine koymayın.
