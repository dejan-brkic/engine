package org.craftercms.engine.navigation.impl;

import java.util.List;

import org.craftercms.commons.converters.Converter;
import org.craftercms.commons.lang.Callback;
import org.craftercms.core.service.Context;
import org.craftercms.core.service.Item;
import org.craftercms.core.util.cache.CacheTemplate;
import org.craftercms.engine.model.SiteItem;
import org.craftercms.engine.navigation.NavItem;
import org.craftercms.engine.service.SiteItemService;
import org.craftercms.engine.service.context.SiteContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by alfonsovasquez on 24/11/16.
 */
public class NavBreadcrumbBuilderImplTest {

    private NavBreadcrumbBuilderImpl navBreadcrumbBuilder;

    @Before
    public void setUp() throws Exception {
        navBreadcrumbBuilder = new NavBreadcrumbBuilderImpl();
        navBreadcrumbBuilder.setCacheTemplate(getCacheTemplate());
        navBreadcrumbBuilder.setSiteItemService(getSiteItemService());
        navBreadcrumbBuilder.setDefaultItemConverter(getItemConverter());

        setUpCurrentSiteContext();
    }

    @Test
    public void testGetBreadcrumb() throws Exception {
        List<NavItem> breadcrumb = navBreadcrumbBuilder.getBreadcrumb("/site/website/en_US/about-us/leadership-team/index.xml",
                                                                      "/site/website/en_US");

        assertNotNull(breadcrumb);
        assertEquals(3, breadcrumb.size());
        assertEquals("/en_US", breadcrumb.get(0).getUrl());
        assertEquals("Home", breadcrumb.get(0).getLabel());
        assertEquals("/en/about-us", breadcrumb.get(1).getUrl());
        assertEquals("About Us", breadcrumb.get(1).getLabel());
        assertEquals("/en/about-us/leadership-team", breadcrumb.get(2).getUrl());
        assertEquals("Leadership Team", breadcrumb.get(2).getLabel());
    }

    protected SiteItemService getSiteItemService() {
        SiteItem enUsItem = mock(SiteItem.class);
        when(enUsItem.getStoreUrl()).thenReturn("/site/website/en_US");
        when(enUsItem.get("navLabel")).thenReturn("Home");
        when(enUsItem.getItem()).thenReturn(mock(Item.class));

        SiteItem aboutUsItem = mock(SiteItem.class);
        when(aboutUsItem.getStoreUrl()).thenReturn("/site/website/en/about-us");
        when(aboutUsItem.get("navLabel")).thenReturn("About Us");
        when(aboutUsItem.getItem()).thenReturn(mock(Item.class));

        SiteItem leadershipTeamItem = mock(SiteItem.class);
        when(leadershipTeamItem.getStoreUrl()).thenReturn("/site/website/en/about-us/leadership-team");
        when(leadershipTeamItem.get("navLabel")).thenReturn("Leadership Team");
        when(leadershipTeamItem.getItem()).thenReturn(mock(Item.class));

        SiteItemService siteItemService = mock(SiteItemService.class);
        when(siteItemService.getSiteItem("/site/website/en_US", null)).thenReturn(enUsItem);
        when(siteItemService.getSiteItem("/site/website/en_US/about-us", null)).thenReturn(aboutUsItem);
        when(siteItemService.getSiteItem("/site/website/en_US/about-us/leadership-team", null)).thenReturn(leadershipTeamItem);

        return siteItemService;
    }

    protected Converter<SiteItem, NavItem> getItemConverter() {
        Converter<SiteItem, NavItem> converter = mock(Converter.class);
        doAnswer(new Answer<NavItem>() {

            @Override
            public NavItem answer(InvocationOnMock invocation) throws Throwable {
                SiteItem siteItem = (SiteItem)invocation.getArguments()[0];
                NavItem navItem = new NavItem();

                navItem.setUrl(siteItem.getStoreUrl().replace("/site/website", ""));
                navItem.setLabel((String)siteItem.get("navLabel"));

                return navItem;
            }

        }).when(converter).convert(any(SiteItem.class));

        return converter;
    }

    protected CacheTemplate getCacheTemplate() {
        CacheTemplate cacheTemplate = mock(CacheTemplate.class);
        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return ((Callback)invocation.getArguments()[1]).execute();
            }

        }).when(cacheTemplate).getObject(any(Context.class), any(Callback.class), anyVararg());

        return cacheTemplate;
    }

    private void setUpCurrentSiteContext() {
        SiteContext siteContext = mock(SiteContext.class);
        when(siteContext.getSiteName()).thenReturn("test");

        SiteContext.setCurrent(siteContext);
    }

}
