/*
 * Copyright 2019 Pnoker. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pnoker.transfer.rtmp.hystrix;

import com.pnoker.common.model.domain.rtmp.Rtmp;
import com.pnoker.common.model.dto.Response;
import com.pnoker.transfer.rtmp.feign.RtmpDbsFeignApi;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>Copyright(c) 2019. Pnoker All Rights Reserved.
 * <p>@Author    : Pnoker
 * <p>Email      : pnokers@gmail.com
 * <p>Description:
 */
@Slf4j
@Component
public class RtmpDbsFeignApiHystrix implements FallbackFactory<RtmpDbsFeignApi> {

    @Override
    public RtmpDbsFeignApi create(Throwable throwable) {
        String message = throwable.getMessage() == null ? "No available server for client: DC3-DBS" : throwable.getMessage();
        log.error("RtmpFeignApi,进入熔断:{}", message, throwable);

        return new RtmpDbsFeignApi() {
            @Override
            public Response list() {
                return fail(throwable);
            }

            @Override
            public Response<Boolean> add(Rtmp rtmp) {
                return null;
            }

            @Override
            public Response delete(String id) {
                return fail(throwable);
            }

            @Override
            public Response<Boolean> update(Rtmp rtmp) {
                return null;
            }
        };
    }

    public Response fail(Throwable throwable) {
        return new Response().failure(throwable.getMessage());
    }
}