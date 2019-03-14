from selenium import webdriver
import pyautogui as p
import codecs
import random
import time
import sys


def getData():  
	ones = int(random.random()*100)
	tens = int(random.random()*100)
	chars = ['a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z']
	name = ""
	size = random.randrange(4, 10)
	for _ in range(size):
		name+=random.choice(chars)

	return (name, ones, tens)

def main():
	browser = webdriver.Firefox()
	browser.get("http://127.0.0.1:8080/Task_3/index.html")

	for _ in range(int(sys.argv[1])):
		name, n1, n2 = getData()
				#Fill all the entires
		user_field = browser.find_element_by_name("user")
		user_field.click()
		p.typewrite(name)

		num1=browser.find_element_by_name("num1")
		num1.click()
		p.typewrite(str(n1))

		num2=browser.find_element_by_name("num2")
		num2.click()
		p.typewrite(str(n2))

	
		#Click on captcha
		time.sleep(1)
		captcha = browser.find_element_by_xpath("/html/body/form/table/tbody/tr[4]/td/div/div/div/iframe")
		action = webdriver.common.action_chains.ActionChains(browser)
		loc = captcha.location
		size = captcha.size
		print (loc)
		x, y = loc['x'], loc['y']
		width, height = size['width'], size['height']
		action.move_to_element_with_offset(captcha, width//2 - 120, height//2)
		action.click()
		action.perform()
		#p.click(x_click, y_click)
		time.sleep(2)

		#See-Result
		btn = browser.find_element_by_class_name("button")
		btn.click()


		link = browser.find_element_by_link_text('again.')
		link.click()

		time.sleep(4)
	#This is page 1

if __name__ == '__main__':
	main()
