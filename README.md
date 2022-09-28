<a name="readme-top"></a>

<!-- PROJECT LOGO -->
<br />
<div align="center">
<img src="https://raw.githubusercontent.com/tosoba/ClipFinder/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" alt="Logo" width="150" height="150">

<h2 align="center">ClipFinder</h2>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
    </li>
    <li><a href="#license">License</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

<div align="center">
<img src="https://raw.githubusercontent.com/tosoba/ClipFinder/master/Screenshot_1.png" alt="Logo" width="270" height="570">
<img src="https://raw.githubusercontent.com/tosoba/ClipFinder/master/Screenshot_2.png" alt="Logo" width="270" height="570">
<img src="https://raw.githubusercontent.com/tosoba/ClipFinder/master/Screenshot_3.png" alt="Logo" width="270" height="570">
</div>

A spotify/soundcloud video clip search app - mostly written in 2019/2020. Spotify playback requires being logged in with a premium account.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started


1. Create a project at [https://console.cloud.google.com/](https://console.cloud.google.com/).
2. Enable Youtube API
3. Paste the API key into `gradle.properties`
   ```properties
   youtube.apiKey=<YOUTUBE_API_KEY>
   ```
4. Create a project at [https://developer.spotify.com/](https://developer.spotify.com/).
5. Paste the client id/secret and redirect URI into `gradle.properties`
   ```properties
   spotify.clientId=<SPOTIFY_CLIENT_ID>
   spotify.clientSecret=<SPOTIFY_CLIENT_SECRET>
   spotify.redirectUri=<SPOTIFY_REDIRECT_URI>
   ```
6. Make sure redirect URI matches intent filter data attribute inside app `AndroidManifest.xml`
   ```xml
   <activity
        android:name="net.openid.appauth.RedirectUriReceiverActivity"
        android:exported="true">

        <intent-filter>
            <action android:name="android.intent.action.VIEW" />

            <category android:name="android.intent.category.DEFAULT" />
            <category android:name="android.intent.category.BROWSABLE" />

            <data
                android:host="<HOST FROM REDIRECT_URI>"
                android:path="<PATH FROM REDIRECT_URI>"
                android:scheme="app" />
        </intent-filter>

   </activity>
   ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.md` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

