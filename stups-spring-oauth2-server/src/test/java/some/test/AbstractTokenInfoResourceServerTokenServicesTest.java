/**
 * Copyright (C) 2016 Zalando SE (http://tech.zalando.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package some.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;
import org.zalando.stups.oauth2.spring.client.StupsOAuth2RestTemplate;
import org.zalando.stups.spring.http.client.ClientHttpRequestFactorySelector;

/**
 * @author jbellmann
 */
public abstract class AbstractTokenInfoResourceServerTokenServicesTest {

    @Value("${local.server.port}")
    protected int port;

    @Test
    public void invokeOAuthSecuredService() {

        RestOperations restOperations = buildClient("123456789");

        ResponseEntity<String> responseEntity = restOperations.getForEntity(getBasePath() + "/secured/hello/bello",
                String.class);

        //
        assertThat(responseEntity.getBody()).isEqualTo("hello bello");

    }

    @Test(expected = HttpClientErrorException.class)
    public void invokeOAuthSecuredServiceWithInvalidToken() {
        RestOperations restOperations = buildClient("error");

        restOperations.getForEntity(getBasePath() + "/secured/hello/bello", String.class);
    }

    @Test(expected = HttpClientErrorException.class)
    public void invokeOAuthSecuredServiceWithInvalidTokenNoUid() {
        RestOperations restOperations = buildClient("no-uid");

        restOperations.getForEntity(getBasePath() + "/secured/hello/bello", String.class);
    }

    @Test(expected = HttpClientErrorException.class)
    public void invokeOAuthSecuredServiceWithInvalidTokenEmptyUid() {
        RestOperations restOperations = buildClient("empty-uid");

        restOperations.getForEntity(getBasePath() + "/secured/hello/bello", String.class);
    }

    // TODO, maybe we should throw InvalidToken if 'scope' does not exist?
    @Test(expected = HttpClientErrorException.class)
    public void invokeOAuthSecuredServiceWithInvalidTokenNoScope() {
        RestOperations restOperations = buildClient("no-scope");

        restOperations.getForEntity(getBasePath() + "/secured/hello/bello", String.class);
    }

    @Test
    public void invokeOAuthSecuredWithRealmServiceWithLaxAuthorizationAndTheValueSetToFalse() {
        RestOperations restOperations = buildClient("123456789");

        restOperations.getForEntity(getBasePath() + "/realmSecured/hello/realm", String.class);
    }

    @Test(expected = HttpClientErrorException.class)
    public void invokeOAuthSecuredWithInvalidRealm() {
        RestOperations restOperations = buildClient("invalidRealm");

        restOperations.getForEntity(getBasePath() + "/realmSecured/hello/realm", String.class);
    }

    @Test(expected = HttpClientErrorException.class)
    public void invokeOAuthSecuredWithNoRealm() {
        RestOperations restOperations = buildClient("noRealm");

        restOperations.getForEntity(getBasePath() + "/realmSecured/hello/realm", String.class);
    }

    @Test
    public void invokeOAuthSecuredWithUidScopeAndRealm() {
        RestOperations restOperations = buildClient("123456789");

        restOperations.getForEntity(getBasePath() + "/combinedRealmSecured/hello/realm", String.class);
    }

    @Test(expected = HttpClientErrorException.class)
    public void invokeOAuthSecuredWithoutUidScopeAndRealm() {
        RestOperations restOperations = buildClient("no-uid");

        restOperations.getForEntity(getBasePath() + "/combinedRealmSecured/hello/realm", String.class);
    }

    protected RestOperations buildClient(final String token) {
        final AccessTokenProvider mockTokenProvider = mock(AccessTokenProvider.class);

        when(mockTokenProvider.obtainAccessToken(isNull(OAuth2ProtectedResourceDetails.class),
                isNull(AccessTokenRequest.class))).thenReturn(new DefaultOAuth2AccessToken(token));

        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactorySelector.getRequestFactory();

        return new StupsOAuth2RestTemplate(mockTokenProvider, requestFactory);
    }

    protected String getBasePath() {
        return "http://localhost:" + port;
    }
}
