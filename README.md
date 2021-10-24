# selenium-docker-example

## クライアントも WebDriver もブラウザ本体もローカルで稼働する場合

macOS を利用している場合は、ChromeDriver などのドライバーを格納したディレクトリで以下のコマンドを実行して、実行可能にしてから使用する。

```bash
sudo xattr -d -r com.apple.quarantine chromedriver
```

Safari は WebDriver は不要で、Safari の設定で「開発」から「リモートオートメーションを許可」とするだけでよい。

Chrome や Firefox(geckodriver) を利用する場合は、WebDriver へのパスを指定する必要がある。

```java
System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
ChromeOptions options = new ChromeOptions();
WebDriver driver = new ChromeDriver(options);
```

## クライアントはローカルで稼働し、 Hub と Node は Grid を構成するコンテナ内で稼働する場合

複数のコンテナが必要なので、docker-compose を利用するのが楽。
以下の yml がそのまま使える。
https://github.com/SeleniumHQ/docker-selenium/blob/trunk/docker-compose-v3.yml

```bash
docker-compose -f docker-compose-v3.yml up
```

Java でクライアントを利用する例。options は必要なものを設定する。最後の URL は Hub にアクセスできる URL を指定する。docker-machine を利用する場合、`docker-machine ip default`の IP アドレス を hosts に記載しておくと楽。RemoteWebDriver を利用する場合は、`webdriver.chrome.driver`のような webdriver のパスの指定は node 側に存在するため、クライアント側は hub の URL のみを指定すれば良い。クライアントのコード上は環境固有の情報がなくなってスッキリするが、ローカルで稼働させた場合よりもパフォーマンスが劣化する。

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
```

Java コードは Grid の例と同じ。

## （補足）Hub と Node を個別に起動する方法

```bash
# hubを起動する
java -jar selenium-server-4.0.0.jar hub
# nodeを起動する
# macOS の場合、Safari は webdriver のパスを指定する必要はない。portとmax-sessionsは適宜設定
java -Dwebdriver.chrome.driver=/path/to/chromedriver -Dwebdriver.gecko.driver=/path/to/geckodriver -jar selenium-server-4.0.0.jar node --hub http://localhost:4444/ --port 5555 --max-sessions 1
```

## （補足）コンテナを用いている場合に操作中の画面を参照する方法

7900 番ポートをブラウザで開くと参照できる(headless にしているとダメ)。macOS で docker-machine を使っている場合は以下のコマンドでコマンドでブラウザを開ける。

```bash
open http://$(docker-machine ip default):7900/
```
