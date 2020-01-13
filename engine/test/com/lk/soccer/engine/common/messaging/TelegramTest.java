/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lk.soccer.engine.common.messaging;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.lk.engine.common.telegraph.Message;
import com.lk.engine.common.telegraph.Telegram;
import com.lk.engine.common.telegraph.TelegramPackage;

/**
 * 
 * @author robot
 */
public class TelegramTest {

	public TelegramTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of equals method, of class Telegram.
	 */
	@Test
	public void testEquals() {
		Telegram t1 = new TelegramPackage(0.5, 1, 2, Message.GO_HOME);
		Telegram t2 = new TelegramPackage(0.5 + Telegram.SMALLEST_DELAY - 0.0001, 1, 2, Message.GO_HOME);
		boolean result = t1.equals(t2);
		assertEquals(true, result);
	}

	/**
	 * Test of hashCode method, of class Telegram.
	 */
	@Test
	public void testHashCode() {
		Telegram t1 = new TelegramPackage(0.5, 1, 2, Message.GO_HOME);
		Telegram t2 = new TelegramPackage(0.5 + Telegram.SMALLEST_DELAY - 0.0001, 1, 2, Message.GO_HOME);
		assertEquals("Equals object must have equal hashCode", t1.hashCode(), t2.hashCode());
	}

	/**
	 * Test of compareTo method, of class Telegram.
	 */
	@Test
	public void testCompareTo() {
		TelegramPackage t1 = new TelegramPackage(0.5, 1, 2, Message.GO_HOME);
		Telegram t2 = new TelegramPackage(0.5, 1, 2, Message.GO_HOME);
		int result = t1.compareTo(t2);
		assertEquals(0, result);
	}
}