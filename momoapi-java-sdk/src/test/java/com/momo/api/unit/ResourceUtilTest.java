package com.momo.api.unit;

import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.util.ResourceUtil;
import com.momo.api.base.util.StringUtils;
import com.momo.api.models.AccountHolder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * Class ResourceUtilTest
 */
public class ResourceUtilTest extends ResourceUtil{
	@Test
	@DisplayName("Generate Resource Path With an AccountHolder Test Success")
	void resourcePathWithAccountHolderTestSuccess() throws MoMoException {
		String expectedPath = "test-path/accountid/1";
        
		String generatedPath = ResourceUtil.getResourcePathAccountHolder("test-path/{accountHolderIdType}/{accountHolderId}", new AccountHolder("accountid", "1"));
		
		assertEquals(expectedPath, generatedPath);
	}
	
	@Test
	@DisplayName("Generate Resource Path With an AccountHolder Test Failure")
	void resourcePathWithAccountHolderTestFailure() throws MoMoException {
		String expectedPath = "test-path/accountid/2";
        
		String generatedPath = ResourceUtil.getResourcePathAccountHolder("test-path/{accountHolderIdType}/{accountHolderId}", new AccountHolder("accountid", "1"));
		
		assertNotEquals(expectedPath, generatedPath);
	}
	
	@Test
	@DisplayName("String Value Is Empty Or Null Test Success")
	void isNullOrEmptyTestSuccess() {
		assertEquals(true, StringUtils.isNullOrEmpty(null));
		assertEquals(true, StringUtils.isNullOrEmpty(""));
		assertEquals(true, StringUtils.isNullOrEmpty(" "));
		assertEquals(false, StringUtils.isNullOrEmpty("momo"));
	}
	
	@Test
	@DisplayName("Check if URL is valid Test Success")
	void validateURLTestSuccess() {
		assertTrue(isValidURL("https://sample.com"));
		assertTrue(isValidURL("http://sample"));
	}
	
	@Test
	@DisplayName("Check if URL is valid Test Fail")
	void validateURLTestFail() {
		assertFalse(isValidURL("https:/sample.com"));
		assertFalse(isValidURL("https:sample.com"));
		assertFalse(isValidURL("https//sample.com"));
		assertFalse(isValidURL("htt://sample.com"));
		assertFalse(isValidURL("//sample.com"));
		assertFalse(isValidURL("https:"));
		assertFalse(isValidURL("sample.com"));
	}
    
}
