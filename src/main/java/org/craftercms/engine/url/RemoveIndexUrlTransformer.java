/*
 * Copyright (C) 2007-2020 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.craftercms.engine.url;

import org.apache.commons.lang3.StringUtils;
import org.craftercms.core.exception.UrlTransformationException;
import org.craftercms.core.service.CachingOptions;
import org.craftercms.core.service.Context;
import org.craftercms.core.url.UrlTransformer;
import org.craftercms.engine.properties.SiteProperties;

/**
 * Created by alfonsovasquez on 7/9/16.
 */
public class RemoveIndexUrlTransformer implements UrlTransformer {

    @Override
    public String transformUrl(Context context, CachingOptions cachingOptions,
                               String url) throws UrlTransformationException {
        return StringUtils.removeEnd(url, SiteProperties.getIndexFileName());
    }

}
