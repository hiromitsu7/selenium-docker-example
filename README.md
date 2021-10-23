# selenium-docker-example

## クライアントはローカルで稼働し、 Hub と Node は Grid を構成するコンテナ内で稼働する場合

複数のコンテナが必要なので、docker-compose を利用するのが楽。
以下の yml がそのまま使える。
https://github.com/SeleniumHQ/docker-selenium/blob/trunk/docker-compose-v3.yml

わかりやすくするためにリネームした。

```bash
docker-compose -f docker-compose-grid.yml up
```

Java でクライアントを利用する例。options は必要なものを設定する。最後の URL は Hub にアクセスできる URL を指定する。docker-machine を利用する場合、`docker-machine ip default`の IP アドレス を hosts に記載しておくと楽。

```java
DesiredCapabilities dc = DesiredCapabilities.chrome();
dc.setBrowserName("chrome");
dc.setPlatform(Platform.LINUX);
ChromeOptions options = new ChromeOptions();
options.addArguments("--headless", "--disable-gpu", "--window-size=1024,768");
dc.setCapability(ChromeOptions.CAPABILITY, options);
WebDriver driver = new RemoteWebDriver(new URL("http://docker-machine:4444"), dc);
```

## クライアントはローカルで稼働し、 Hub と Node は standalone コンテナ内で稼働する場合

Hub と Node は standalone コンテナに入っているのでコンテナ一つを稼働すれば良い。

```bash
# docker-composeを用いない場合
docker run --rm -d -p 4444:4444 -p 7900:7900 --shm-size=2gb selenium/standalone-chrome:4.0.0-20211013
# docker-composeを用いる場合
docker-compose -f docker-compose-standalone-chrome.yml up
```

Java コードは Grid の例と同じ。

## クライアントも WebDriver もブラウザ本体もローカルで稼働する場合

macOS を利用している場合は、ChromeDriver などのドライバーを格納したディレクトリで以下のコマンドを実行して、実行可能にしてから使用する。
Safari は WebDriver は不要で、Safari の設定で「開発」から「リモートオートメーションを許可」とするだけでよい。

```bash
sudo xattr -d -r com.apple.quarantine chromedriver
```

WebDriver へのパスを指定する必要がある。

```java
System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
ChromeOptions options = new ChromeOptions();
WebDriver driver = new ChromeDriver(options);
```

## （補足）コンテナを用いている場合に操作中の画面を見たい場合

7900 番ポートをブラウザで開くと参照できる(headless にしているとダメ)。macOS で docker-machine を使っている場合は以下のコマンドでコマンドでブラウザを開ける。

```bash
open http://$(docker-machine ip default):7900/
```
