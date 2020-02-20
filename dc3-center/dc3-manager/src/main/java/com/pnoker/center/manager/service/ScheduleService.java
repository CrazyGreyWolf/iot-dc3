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

package com.pnoker.center.manager.service;

import com.pnoker.common.base.Service;
import com.pnoker.common.dto.ScheduleDto;
import com.pnoker.common.model.Schedule;

/**
 * <p>Schedule Interface
 *
 * @author pnoker
 */
public interface ScheduleService extends Service<Schedule, ScheduleDto> {
    /**
     * 根据设备分组NAME查询分组
     *
     * @param name
     * @return
     */
    Schedule selectByName(String name);

}