package com.lotto.cache.redis;

import com.lotto.BaseIntegrationTest;
import com.lotto.IntegrationTestData;
import com.lotto.domain.resultannouncer.ResultAnnouncerFacade;
import com.lotto.infrastructure.loginandregister.controller.dto.JwtResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RedisResultCacheIntegrationTest extends BaseIntegrationTest implements IntegrationTestData {

    @Container
    private static final GenericContainer<?> REDIS;

    @SpyBean
    ResultAnnouncerFacade resultAnnouncerFacade;

    @Autowired
    CacheManager cacheManager;

    static {
        REDIS = new GenericContainer<>("redis").withExposedPorts(6379);
        REDIS.start();
    }

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.data.redis.port", () -> REDIS.getFirstMappedPort().toString());
        registry.add("spring.cache.type", () -> "redis");
        registry.add("spring.cache.redis.time-to-live", () -> "PT1S");
    }

    @Test
    public void should_save_result_to_cache_and_then_invalidate_by_time_to_live() throws Exception {
        // step 1: someUser was registered with somePassword
        // given && when
        mockMvc.perform(post("/register").content(requestBodyRegister())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());


        // step 2: login
        // given && when
        ResultActions successLoginRequest = mockMvc.perform(post("/token").content(requestBodyLogin())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        // then
        MvcResult mvcResult = successLoginRequest.andExpect(status().isOk()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        JwtResponseDto jwtResponse = objectMapper.readValue(json, JwtResponseDto.class);
        String jwtToken = jwtResponse.token();


        //step 3: should save to cache result request
        //given && when
        mockMvc.perform(get("/result/hash")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        //then
        verify(resultAnnouncerFacade, times(1)).checkResult("hash");
        assertThat(cacheManager.getCacheNames().contains("resultAnnounce")).isTrue();


        //step 4: cache should be invalidated
        //given && when && then
        await()
                .atMost(Duration.ofSeconds(4))
                .pollInterval(Duration.ofSeconds(1))
                .untilAsserted(() -> {
                    mockMvc.perform(get("/result/hash")
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType(MediaType.APPLICATION_JSON_VALUE));
                    verify(resultAnnouncerFacade, atLeast(2)).checkResult("hash");
                });
    }
}
