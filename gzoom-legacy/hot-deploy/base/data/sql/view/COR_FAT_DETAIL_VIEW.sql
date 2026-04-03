CREATE VIEW COR_FAT_DETAIL_VIEW AS
SELECT
    pdo.CUSTOM_TIME_PERIOD_CODE AS ANNO,
    are.ETCH AS ETCH_AREA_RISCHIO,
    are.WORK_EFFORT_NAME AS AREA_RISCHIO,
    pro.SOURCE_REFERENCE_ID AS COD_PROCESSO,
    pro.ETCH AS ETCH_PROCESSO,
    pro.WORK_EFFORT_NAME AS PROCESSO,
    fas.WORK_EFFORT_NAME AS FASE_O_SOTTOPROCESSO,
    ris.WORK_EFFORT_NAME AS RISCHIO,
    acc.ACCOUNT_NAME AS FATTORE_RISCHIO,
    CONCAT('Lang. ', pdo.CUSTOM_TIME_PERIOD_CODE) AS ANNO_LANG,
    CONCAT('Lang. ', are.WORK_EFFORT_NAME) AS AREA_RISCHIO_LANG,
    CONCAT('Lang. ', pro.WORK_EFFORT_NAME) AS PROCESSO_LANG,
    CONCAT('Lang. ', fas.WORK_EFFORT_NAME) AS FASE_O_SOTTOPROCESSO_LANG,
    CONCAT('Lang. ', ris.WORK_EFFORT_NAME) AS RISCHIO_LANG,
    CONCAT('Lang. ', acc.ACCOUNT_NAME) AS FATTORE_RISCHIO_LANG,
    pdo.THRU_DATE AS DATA_RIFERIMENTO,
    are.WORK_EFFORT_ID AS ARE_ID,
    pro.WORK_EFFORT_ID AS PRO_ID,
    pro.ORG_UNIT_ID AS PRO_RESP,
    fas.WORK_EFFORT_ID AS FAS_ID,
    fas.ORG_UNIT_ID AS FAS_RESP,
    ris.WORK_EFFORT_ID AS RIS_ID,
    ris.ORG_UNIT_ID AS RIS_RESP,
    acc.GL_ACCOUNT_ID AS FAT_ID
FROM
    work_effort ris
JOIN work_effort_type rispdo ON rispdo.WORK_EFFORT_TYPE_ID = ris.WORK_EFFORT_TYPE_ID
JOIN custom_time_period pdo ON pdo.PERIOD_TYPE_ID = rispdo.PERIOD_TYPE_ID
    AND pdo.FROM_DATE <= ris.ESTIMATED_COMPLETION_DATE
    AND pdo.THRU_DATE >= ris.ESTIMATED_START_DATE
JOIN work_effort_measure rism ON rism.WORK_EFFORT_ID = ris.WORK_EFFORT_ID
JOIN work_effort_purpose_account pur ON pur.GL_ACCOUNT_ID = rism.GL_ACCOUNT_ID
    AND pur.WORK_EFFORT_PURPOSE_TYPE_ID = 'CORFAT'
JOIN acctg_trans_entry ate ON ate.VOUCHER_REF = rism.WORK_EFFORT_MEASURE_ID
    AND ate.AMOUNT = 100 and ate.work_effort_revision_id is null
JOIN acctg_trans att ON att.ACCTG_TRANS_ID = ate.ACCTG_TRANS_ID
    AND att.TRANSACTION_DATE = pdo.THRU_DATE and att.work_effort_revision_id is null
JOIN gl_account acc ON acc.GL_ACCOUNT_ID = ate.GL_ACCOUNT_ID
JOIN work_effort_assoc fasris ON fasris.WORK_EFFORT_ID_TO = ris.WORK_EFFORT_ID
	and fasris.work_effort_revision_id is null
JOIN work_effort fas ON fas.WORK_EFFORT_ID = fasris.WORK_EFFORT_ID_FROM
JOIN work_effort_assoc profas ON profas.WORK_EFFORT_ID_TO = fas.WORK_EFFORT_ID
	and profas.work_effort_revision_id is null
JOIN work_effort pro ON pro.WORK_EFFORT_ID = profas.WORK_EFFORT_ID_FROM
JOIN work_effort_assoc arepro ON arepro.WORK_EFFORT_ID_TO = pro.WORK_EFFORT_ID
	and arepro.work_effort_revision_id is null
JOIN work_effort are ON are.WORK_EFFORT_ID = arepro.WORK_EFFORT_ID_FROM
where
	ris.work_effort_revision_id is null
	and fasris.WORK_EFFORT_ASSOC_TYPE_ID = '15APPRO'
    AND profas.WORK_EFFORT_ASSOC_TYPE_ID = '15APPRO'
    AND arepro.WORK_EFFORT_ASSOC_TYPE_ID = '15APPPC';