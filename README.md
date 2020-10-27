# SafexPay-Android-SDK
<h2>Introduction</h2>
<br>Are you looking for a way to accept payments from mobile users in different countries, different payment system by writing just few of code lines? If yes then you landed the correct repo.
SafeXPay Android SDK makes the job possible and easier for you by providing you a globala mobile payment gateway that accepts payments from almost all the countries with 100+ alternative payment options.
By making our Android SDK a native part of your application, you can eliminate the necessity to open a web browser for payments(which is certainly insecure).
<br>What's more? Less steps, faster process and assuredly a boost in your conversion rate!
<h2>Installation</h2>
<h3>Requirements</h3>
<ul>
<li>Android 5.0 (API level 21) and above</li>
<li><a href="https://developer.android.com/studio/releases/gradle-plugin" rel="nofollow">Android Gradle Plugin</a> 3.5.1</li>
<li><a href="https://gradle.org/releases/" rel="nofollow">Gradle</a> 5.6.4-all+</li>
<li><a href="https://developer.android.com/jetpack/androidx/" rel="nofollow">AndroidX</a></li>
<li><a href="https://docs.gradle.org/current/userguide/java_plugin.html#sec:java-extension" rel="nofollow">Contributed extension</a> VERSION_1_8</li>
</ul>
<h3>Configuration</h3>
To get <code>SafeXPay Android SDK</code> into your project:<br>
<ul>
<li>Add the JitPack repository to your build file<br>
<pre><code>allprojects {
repositories {
      ...
      maven { url 'https://jitpack.io' }
   }
}</code></pre>
</li>
<li>Add the <code>SafeXPay dependency</code> to your app level<code>build.gradle</code> dependencies.<br><br>
<pre><code>dependencies {
    ...
    implementation 'com.github.antinolabs:SafexPay-Android-SDK:<a href="https://github.com/antinolabs/SafexPay-Android-SDK/releases" rel="nofollow">latest_version</a>'
    ...
  }</code></pre>
</li>
</ul>

<h2>Getting Started</h2>
The entry point to the SafeXPay SDK is the <code>SafeXPay</code> class. It must be initialized with the <i>Environment(TEST/PRODUCTION)</i>, <i>merchant_id</i>, <i>merchant_key</i>.<br>
<pre><code>SafeXPay.getInstance().initialize(this, SafeXPay.Environment.TEST(or SafeXPay.Environment.PRODUCTION),
    "ag_id", "merchant_id", "merchant_key")</code></pre>
Then while making a payment, you need to call the Initiate Method of the <code>SafeXPay</code> class.
<pre><code>SafeXPay.getInstance().initiatePayment(context_of_calling_activity, order_no, amount, currency,
     transaction_type, channel<i>(MOBILE)</i>, success_url, failure_url, country_code, SafeXPayPaymentCallback_interface_context)</code></pre>
<br>
By <a href="https://www.antino.io/">Antino Labs</a>
