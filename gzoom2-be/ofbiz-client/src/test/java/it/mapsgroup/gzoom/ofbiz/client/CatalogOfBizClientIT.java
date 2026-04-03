package it.mapsgroup.gzoom.ofbiz.client;

import com.google.common.collect.MapDifference;
import it.mapsgroup.gzoom.ofbiz.client.impl.CatalogOfBizClientImpl;
import it.mapsgroup.gzoom.ofbiz.util.MapUtil;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsIterableContaining.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;


/**
 * 20/02/13
 *
 * @author Andrea Fossi
 */
public class CatalogOfBizClientIT extends AbstractOfBizTest {
    private CatalogOfBizClient catalogOfBizClient;

    private static final Logger log = LoggerFactory
            .getLogger(CatalogOfBizClientIT.class);


    @Before
    public void setUp() {
        catalogOfBizClient = new CatalogOfBizClientImpl(config, connectionManager);
    }


    /**
     * Test Company creation
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testFindSecurityGroups() {
        String sessionId = login();
        String context = "CONTEXT1";
        try {
            Map<String, Object> serviceResponse = catalogOfBizClient.getRoles(new HashMap<String, Object>(), sessionId, context);
            Map<String, Object> attemptedResponse = (Map<String, Object>) fromXml("/ofbiz-entity/Service-GetRoles-Result.xml");
            Map<String, Map<String, Object>> groups = MapUtil.listToMap((List<Map<String, Object>>) serviceResponse.get("securityGroups"), "groupId", String.class);
            Map<String, Map<String, Object>> attemptedGroups = MapUtil.listToMap((List<Map<String, Object>>) attemptedResponse.get("securityGroups"), "groupId", String.class);
            assertThat("security groups are the same", groups.keySet(), is(attemptedGroups.keySet()));
            for (String key : groups.keySet()) {
                MapDifference<String, Object> responseDiff = MapUtil.difference(groups.get(key), attemptedGroups.get(key));
                assertTrue("result is not equals" + responseDiff.entriesDiffering().toString(), responseDiff.areEqual());
            }
        } finally {
            logOff(sessionId);
        }

    }

    /**
     * Test Company creation
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetWaste() {
        String sessionId = login();
        String context = "CONTEXT1";
        try {
            Map<String, Object> serviceResponse = catalogOfBizClient.getWastes(new HashMap<String, Object>(), sessionId, context);
            List<Map<String, Object>> cers = (List<Map<String, Object>>) serviceResponse.get("wasteCerCodes");
            cers = MapUtil.copyKeys(cers, "wasteCerCodeId", "description", "cerCode", "hazardous");

            List<Map<String, Object>> attemptedCers = (List<Map<String, Object>>) fromXml("/ofbiz-entity/Service-GetWaste-Result.xml");
            Map<String, Map<String, Object>> wantedCersMap = MapUtil.listToMap(attemptedCers, "wasteCerCodeId", String.class);
            for (Map<String, Object> cer : cers) {
                MapDifference<String, Object> diff = MapUtil.difference(cer, wantedCersMap.get(cer.get("wasteCerCodeId")));
                assertTrue("result is not equals. " + diff.toString(), diff.areEqual());
            }
        } finally {
            logOff(sessionId);
        }


    }

    /**
     * Test Company creation
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetOperations() {
        String sessionId = login();
        String context1 = "CONTEXT1";
        try {
            Map<String, Object> serviceResponse = catalogOfBizClient.getOperations(new HashMap<String, Object>(), sessionId, context1);
            List<Map<Object, Object>> operations = (List<Map<Object, Object>>) serviceResponse.get("operations");
            operations = MapUtil.copyKeys(operations, "operationId", "operationTypeId", "operationCode", "description");

            List<Map<String, Object>> attemptedoperations = (List<Map<String, Object>>) fromXml("/ofbiz-entity/Service-GetOperations-Result.xml");

            MapDifference<String, Object> responseDiff = MapUtil.difference(MapUtil.toMap("operations", (Object) operations), MapUtil.toMap("operations", (Object) attemptedoperations));
            assertTrue("result is not equals", responseDiff.areEqual());
        } finally {
            logOff(sessionId);
        }


    }

    /**
     * Test Company creation
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetIssuers() {
        String sessionId = login();
        String context1 = "CONTEXT1";
        try {
            Map<String, Object> serviceResponse = catalogOfBizClient.getIssuers(new HashMap<String, Object>(), sessionId, context1);

            List<Map<Object, Object>> issuers = (List<Map<Object, Object>>) serviceResponse.get("partyNodes");
            assertThat("issuers are four", issuers.size(), is(5));
            Map<String, Map<Object, Object>> issuersMap = MapUtil.listToMap(issuers, "partyId", String.class);
            assertThat("issuers are four", issuersMap.keySet(),
                    containsInAnyOrder("GN_ISS_MATTM", "GN_ISS_REGIONE", "GN_ISS_PROVINCIA", "GN_ALBOS", "GN_ISS_FOREIGN"));
            List<Map<String, Object>> albos = MapUtil.getValue(issuersMap, "GN_ALBOS.partyRelationships", List.class);
            assertThat(albos.size(), is(21));
            assertThat(MapUtil.getValue(issuersMap, "GN_ALBOS.partyRelationships[0].target.partyId", String.class), is(not(nullValue())));

            List<Map<String, Object>> prov = MapUtil.getValue(issuersMap, "GN_ISS_PROVINCIA.partyRelationships", List.class);
            assertThat(prov.size(), is(110));
            assertThat(MapUtil.getValue(issuersMap, "GN_ISS_PROVINCIA.partyRelationships[0].target.partyId", String.class), is(not(nullValue())));

            List<Map<String, Object>> reg = MapUtil.getValue(issuersMap, "GN_ISS_REGIONE.partyRelationships", List.class);
            assertThat(reg.size(), is(20));
            assertThat(MapUtil.getValue(issuersMap, "GN_ISS_REGIONE.partyRelationships[0].target.partyId", String.class), is(not(nullValue())));

            List<Map<String, Object>> matt = MapUtil.getValue(issuersMap, "GN_ISS_MATTM.partyRelationships", List.class);
            assertThat(matt.size(), is(1));
            assertThat(MapUtil.getValue(issuersMap, "GN_ISS_MATTM.partyRelationships[0].target.partyId", String.class), is("GN_ISS_MATTM_ROMA"));

            List<Map<String, Object>> foreign = MapUtil.getValue(issuersMap, "GN_ISS_FOREIGN.partyRelationships", List.class);
            assertThat(foreign.size(), is(1));
            assertThat(MapUtil.getValue(issuersMap, "GN_ISS_FOREIGN.partyRelationships[0].target.partyId", String.class), is("GN_ISS_FOREIGN_BASE"));
        } finally {
            logOff(sessionId);
        }

    }

    /**
     * Test Company creation
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetPackagings() {
        String sessionId = login();
        String context1 = "CONTEXT1";
        try {
            Map<String, Object> serviceResponse = catalogOfBizClient.getPackagings(new HashMap<String, Object>(), sessionId, context1);

            List<Map<Object, Object>> packagings = (List<Map<Object, Object>>) serviceResponse.get("packagings");
            assertThat("packagings are seven", packagings.size(), is(7));
            Map<String, Map<Object, Object>> issuersMap = MapUtil.listToMap(packagings, "packagingId", String.class);
            assertThat("packagings ", issuersMap.keySet(),
                    hasItems("BIGBAG", "CASSONE", "CISTERNA", "FUSTO", "PALLET", "SFUSO", "ALTRO"));
        } finally {
            logOff(sessionId);
        }

    }

    /**
     * Test Company creation
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetCountries() {
        String sessionId = login();
        String context1 = "CONTEXT1";
        try {
            Map<String, Object> serviceResponse = catalogOfBizClient.getCountries(new HashMap<String, Object>(), sessionId, context1);

            List<Map<Object, Object>> _countries = (List<Map<Object, Object>>) serviceResponse.get("countries");
            Map<String, Map<Object, Object>> countries = MapUtil.listToMap(_countries, "geoId", String.class);
            Map<String, Map<String, Object>> regions = MapUtil.listToMap(MapUtil.getValue(countries, "ITA.regions", List.class), "geoId", String.class);
            assertThat(regions.size(), is(20));
            Map<String, Map<String, Object>> provinces = MapUtil.listToMap(MapUtil.getValue(regions, "IT-GN-TOSCANA.provinces", List.class), "geoId", String.class);
            assertTrue(provinces.containsKey("IT-GN-100"));
            assertThat(provinces.size(), is(10));
            Map<String, Map<String, Object>> municipalities = MapUtil.listToMap(MapUtil.getValue(provinces, "IT-GN-100.municipalities", List.class), "geoId", String.class);
            assertTrue(municipalities.containsKey("IT-GN-100007"));
            assertThat(MapUtil.getValue(municipalities, "IT-GN-100007.geoName", String.class), is("Vernio"));

            Map<String, Map<String, Object>> postalCodes = MapUtil.listToMap(MapUtil.getValue(municipalities, "IT-GN-100007.postalCodes", List.class), "geoId", String.class);
            assertTrue(postalCodes.containsKey("IT-GN-CAP-59024"));
            assertThat(MapUtil.getValue(postalCodes, "IT-GN-CAP-59024.geoName", String.class), is("59024"));
        } finally {
            logOff(sessionId);
        }

    }

    /**
     * Test Company creation
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetCategories() {
        String sessionId = login();
        String context1 = "CONTEXT1";
        try {
            Map<String, Object> serviceResponse = catalogOfBizClient.getCategories(new HashMap<String, Object>(), sessionId, context1);

            List<Map<String, Object>> categoriesAndClasses = (List<Map<String, Object>>) serviceResponse.get("categoriesAndClasses");
            assertThat("categoriesAndClasses are 112", categoriesAndClasses.size(), is(112));
            //get first line and check contents
            Map<String, Object> cat1 = categoriesAndClasses.get(0);
            assertThat(cat1.get("lastUpdatedStamp"), is(notNullValue()));
            assertThat(cat1.get("lastUpdatedTxStamp"), is(notNullValue()));
            assertThat(cat1.get("createdTxStamp"), is(notNullValue()));
            assertThat(cat1.get("createdStamp"), is(notNullValue()));
            assertThat((String) cat1.get("subCategoryCode"), is(emptyOrNullString()));
            assertThat((String) cat1.get("categoryDescription"), is("Raccolta e trasporto di rifiuti urbani ed assimilati"));
            assertThat((String) cat1.get("enumId"), is("GN_CAT_1_A"));
            assertThat((String) cat1.get("classCode"), is("A"));
            assertThat((String) cat1.get("categoryCode"), is("1"));
            assertThat((String) cat1.get("sequenceId"), is("0"));
            assertThat((String) cat1.get("classDescription"), is("500.000 o più abitanti"));
        } finally {
            logOff(sessionId);
        }

    }

}
