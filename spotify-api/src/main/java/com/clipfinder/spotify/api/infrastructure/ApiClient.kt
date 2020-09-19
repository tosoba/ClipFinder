package com.clipfinder.spotify.api.infrastructure

import com.clipfinder.spotify.api.auth.OAuth
import com.clipfinder.spotify.api.auth.OAuth.AccessTokenListener
import com.clipfinder.spotify.api.auth.OAuthFlow
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.oltu.oauth2.client.request.OAuthClientRequest.AuthenticationRequestBuilder
import org.apache.oltu.oauth2.client.request.OAuthClientRequest.TokenRequestBuilder
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class ApiClient(
    private var baseUrl: String = defaultBasePath,
    private val okHttpClientBuilder: OkHttpClient.Builder? = null,
    private val serializerBuilder: Moshi.Builder = Serializer.moshiBuilder,
    private val okHttpClient: OkHttpClient? = null
) {
    private val apiAuthorizations = mutableMapOf<String, Interceptor>()
    var logger: ((String) -> Unit)? = null

    private val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(serializerBuilder.build()))
    }

    private val clientBuilder: OkHttpClient.Builder by lazy {
        okHttpClientBuilder ?: defaultClientBuilder
    }

    private val defaultClientBuilder: OkHttpClient.Builder by lazy {
        OkHttpClient()
            .newBuilder()
            .addInterceptor(HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    logger?.invoke(message)
                }
            }).apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
    }

    init {
        normalizeBaseUrl()
    }

    constructor(
        baseUrl: String = defaultBasePath,
        okHttpClientBuilder: OkHttpClient.Builder? = null,
        serializerBuilder: Moshi.Builder = Serializer.moshiBuilder,
        authNames: Array<String>
    ) : this(baseUrl, okHttpClientBuilder, serializerBuilder) {
        authNames.forEach { authName ->
            val auth = when (authName) {
                "spotify_auth" -> OAuth(OAuthFlow.accessCode, "https://accounts.spotify.com/authorize", "https://accounts.spotify.com/api/token", "playlist-read-private, playlist-read-collaborative, ugc-image-upload, user-follow-read, playlist-modify-private, user-read-email, user-read-private, app-remote-control, streaming, user-follow-modify, user-modify-playback-state, user-library-read, user-library-modify, playlist-modify-public, user-read-playback-state, user-read-currently-playing, user-read-recently-played, user-read-playback-position, user-top-read")
                else -> throw RuntimeException("auth name $authName not found in available auth names")
            }
            addAuthorization(authName, auth)
        }
    }

    constructor(
        baseUrl: String = defaultBasePath,
        okHttpClientBuilder: OkHttpClient.Builder? = null,
        serializerBuilder: Moshi.Builder = Serializer.moshiBuilder,
        authName: String,
        clientId: String,
        secret: String,
        username: String,
        password: String
    ) : this(baseUrl, okHttpClientBuilder, serializerBuilder, arrayOf(authName)) {
        getTokenEndPoint()
            ?.setClientId(clientId)
            ?.setClientSecret(secret)
            ?.setUsername(username)
            ?.setPassword(password)
    }

    /**
     * Helper method to configure the token endpoint of the first oauth found in the apiAuthorizations (there should be only one)
     * @return Token request builder
     */
    fun getTokenEndPoint(): TokenRequestBuilder? {
        var result: TokenRequestBuilder? = null
        apiAuthorizations.values.runOnFirst<Interceptor, OAuth> {
            result = tokenRequestBuilder
        }
        return result
    }

    /**
     * Helper method to configure authorization endpoint of the first oauth found in the apiAuthorizations (there should be only one)
     * @return Authentication request builder
     */
    fun getAuthorizationEndPoint(): AuthenticationRequestBuilder? {
        var result: AuthenticationRequestBuilder? = null
        apiAuthorizations.values.runOnFirst<Interceptor, OAuth> {
            result = authenticationRequestBuilder
        }
        return result
    }

    /**
     * Helper method to pre-set the oauth access token of the first oauth found in the apiAuthorizations (there should be only one)
     * @param accessToken Access token
     * @return ApiClient
     */
    fun setAccessToken(accessToken: String): ApiClient {
        apiAuthorizations.values.runOnFirst<Interceptor, OAuth> {
            setAccessToken(accessToken)
        }
        return this
    }

    /**
     * Helper method to configure the oauth accessCode/implicit flow parameters
     * @param clientId Client ID
     * @param clientSecret Client secret
     * @param redirectURI Redirect URI
     * @return ApiClient
     */
    fun configureAuthorizationFlow(clientId: String, clientSecret: String, redirectURI: String): ApiClient {
        apiAuthorizations.values.runOnFirst<Interceptor, OAuth> {
            tokenRequestBuilder
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectURI(redirectURI)
            authenticationRequestBuilder
                ?.setClientId(clientId)
                ?.setRedirectURI(redirectURI)
        }
        return this
    }

    /**
     * Configures a listener which is notified when a new access token is received.
     * @param accessTokenListener Access token listener
     * @return ApiClient
     */
    fun registerAccessTokenListener(accessTokenListener: AccessTokenListener): ApiClient {
        apiAuthorizations.values.runOnFirst<Interceptor, OAuth> {
            registerAccessTokenListener(accessTokenListener)
        }
        return this
    }

    /**
     * Adds an authorization to be used by the client
     * @param authName Authentication name
     * @param authorization Authorization interceptor
     * @return ApiClient
     */
    fun addAuthorization(authName: String, authorization: Interceptor): ApiClient {
        if (apiAuthorizations.containsKey(authName)) {
            throw RuntimeException("auth name $authName already in api authorizations")
        }
        apiAuthorizations[authName] = authorization
        clientBuilder.addInterceptor(authorization)
        return this
    }

    fun setLogger(logger: (String) -> Unit): ApiClient {
        this.logger = logger
        return this
    }

    fun <S> createService(serviceClass: Class<S>): S {
        var usedClient: OkHttpClient? = null
        this.okHttpClient?.let { usedClient = it } ?: run { usedClient = clientBuilder.build() }
        return retrofitBuilder.client(usedClient).build().create(serviceClass)
    }

    private fun normalizeBaseUrl() {
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/"
        }
    }

    private inline fun <T, reified U> Iterable<T>.runOnFirst(callback: U.() -> Unit) {
        for (element in this) {
            if (element is U) {
                callback.invoke(element)
                break
            }
        }
    }

    companion object {
        @JvmStatic
        val defaultBasePath: String by lazy {
            System.getProperties().getProperty("com.clipfinder.spotify.api.baseUrl", "https://api.spotify.com/v1")
        }
    }
}