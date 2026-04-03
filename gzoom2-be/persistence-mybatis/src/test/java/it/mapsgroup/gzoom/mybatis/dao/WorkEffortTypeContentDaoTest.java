package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortTypeContent;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortTypeContentMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class WorkEffortTypeContentDaoTest {

    @Mock
    private WorkEffortTypeContentMapper workEffortTypeContentMapper;

    @InjectMocks
    private WorkEffortTypeContentDao workEffortTypeContentDao;

    @BeforeAll
    public static void beforeAll() {
        MockitoAnnotations.openMocks(WorkEffortTypeContentDaoTest.class);
    }

    @ParameterizedTest(name = "{index} => input={0}, expected={1}")
    @CsvSource({
            "'hideRootEtch=\"Y\";',1",
            "'hideRootEtch=\"Y\"; ',1",
            "'hideRootEtch=\"Y\";hideRoot=\"Y\"; ',2",
    })
    void getWorkEffortTypeContentParams(String input, int expected) {
        Map<String, String> params = Arrays.asList(input.trim().split(";")).stream().map(s -> s.split("=")).collect(Collectors.toMap(e -> e[0].trim(), e -> e[1].replaceAll("\"", "")));
        assertEquals(expected, params.size());
    }

    @ParameterizedTest
    @MethodSource("workEffortTypeContentProvider")
    void testGetWorkEffortTypeContentParams(WorkEffortTypeContentRecord testRecord) {
        Mockito.when(workEffortTypeContentMapper.selectByPrimaryKey(testRecord.workEffortTypeContent.getWorkEffortTypeId(), testRecord.workEffortTypeContent.getContentId()))
                .thenReturn(testRecord.workEffortTypeContent);
        Map<String, String> result = workEffortTypeContentDao.getWorkEffortTypeContentParams(testRecord.workEffortTypeContent.getWorkEffortTypeId(), testRecord.workEffortTypeContent.getContentId());
        assertEquals(testRecord.expectedTokens, result.size());
    }

    static Stream<Arguments> workEffortTypeContentProvider() {
        return Stream.of(
                Arguments.of(new WorkEffortTypeContentRecord(getWorkEffortTypeContent("1","1","hideRootEtch=Y;"), 1)),
                Arguments.of(new WorkEffortTypeContentRecord(getWorkEffortTypeContent( "2", "2", "hideRootEtch=Y; "), 1)),
                Arguments.of(new WorkEffortTypeContentRecord(getWorkEffortTypeContent( "3", "3", "hideRootEtch=Y;hideRoot=N; "), 2)),
                Arguments.of(new WorkEffortTypeContentRecord(getWorkEffortTypeContent( "4", "4", "hideRootEtch=Y;hideRoot=N; RootEtch=Y; "), 3))
        );
    }

    private record WorkEffortTypeContentRecord (
            WorkEffortTypeContent workEffortTypeContent,
            int expectedTokens
    ) {}

    private static WorkEffortTypeContent getWorkEffortTypeContent(String workEffortTypeId, String contentId, String params) {
        WorkEffortTypeContent w = new WorkEffortTypeContent();
        w.setContentId(contentId);
        w.setWorkEffortTypeId(workEffortTypeId);
        w.setParams(params);
        return w;
    }
}