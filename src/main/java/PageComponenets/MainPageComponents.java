package PageComponenets;

import net.jodah.failsafe.internal.util.Assert;
import org.openqa.selenium.WebElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import PageObjects.MainPageObjects;
import Base.BaseComponents;


public class MainPageComponents extends BaseComponents {

    private MainPageObjects mainPageObjects = new MainPageObjects();
    private String pageUrl = "https://automation.herolo.co.il/";
    private String requiredFieldMsg = "שדה * הוא שדה חובה";
    private String illegalEmailMsg = "כתובת אימייל לא חוקית";
    private String illegalPhoneMsg = "מספר טלפון לא חוקי";
    private String weAreHeroloText = "אנחנו הירולו";
    private String activeDot = "slick-active";
    private int popupLoadingTime = 30;
    private List<String> outsidePageLinks = Arrays.asList("https://api.whatsapp.com/send?phone=972544945333",
            "https://www.linkedin.com/company/herolo/",
            "https://api.whatsapp.com/send?phone=972544945333",
            "https://www.facebook.com/Herolofrontend",
            "https://herolo.co.il/?lang=he");

    private static MainPageComponents mainPageComponents;

    private MainPageComponents(){}
    public static MainPageComponents getMainPageComponents(){
        if(mainPageComponents == null){
            mainPageComponents = new MainPageComponents();
        }
        return mainPageComponents;
    }


     public enum FormType {
         HELP_FORM,
         CONTACT_US_FORM,
         POPUP_FORM
    }

    public String getPageURL(){
        return pageUrl;
    }

    /**
     * The function fills "How To Help" form.
     * @param name The name text box value.
     * @param email The email text box value.
     * @param phone The phone number text box value.
     */
    public void fillHelpForm(String name, String email, String phone){
        selenium.elementSendText(mainPageObjects.helpFormNameTextBox,name);
        selenium.elementSendText(mainPageObjects.helpFormEmailTextBox,email);
        selenium.elementSendText(mainPageObjects.helpFormPhoneTextBox,phone);
    }


    /**
     * The function sends "How To Help" form.
     */
    public void sendHelpForm(){
        selenium.clickOnElement(mainPageObjects.helpFormSendBtn);
    }

    /**
     * The function scrolls down the page to "Contact Us" form.
     */
    public void scrollDownToContactForm(){
        selenium.scrollToElement(mainPageObjects.contactUsSendBtn);
    }

    /**
     * The function fills "Contact Us" form.
     * @param name The name text box value.
     * @param company The company text box value.
     * @param email The email text box value.
     * @param phone The phone number text box value.
     */
    public void fillContactUsForm(String name,String company,String email,String phone){
        selenium.elementSendText(mainPageObjects.contactUsNameTextBox,name);
        selenium.elementSendText(mainPageObjects.contactUsCompanyTextBox,company);
        selenium.elementSendText(mainPageObjects.contactUsEmailTextBox,email);
        selenium.elementSendText(mainPageObjects.contactUsPhoneTextBox,phone);
    }

    /**
     * The function sends "How To Help" form.
     */
    public void sendContactUsForm(){
        selenium.clickOnElement(mainPageObjects.contactUsSendBtn);
    }

    /**
     * The function verifies errors of missing fields.
     * @param form The type of form to check the errors for.
     */
    public void MissingRequiredFieldsErrorsCheck(FormType form){
         List<WebElement> elements;
         List<String> errors = fillErrorList(Arrays.asList("שם","אימייל","טלפון"));

         if(form == FormType.HELP_FORM)
             elements = selenium.findElementsByXpath(mainPageObjects.helpFormNameErrorMsg);
         else if(form==FormType.CONTACT_US_FORM){
             elements = selenium.findElementsByXpath(mainPageObjects.contactUsErrorMsgs);
             errors.add(1, "שדה חברה הוא שדה חובה");
         }
         else
             elements = selenium.findElementsByXpath(mainPageObjects.popupErrorMsgs);

         selenium.verifyElementsTexts(elements,errors);
    }

    /**
     * The function verifies error messages of invalid values in text fields.
     * @param form The type of form.
     */
    public void invalidFieldsErrorCheck(FormType form){
        List<WebElement> elements;
        if(form == FormType.HELP_FORM)
            elements = selenium.findElementsByXpath(mainPageObjects.helpFormNameErrorMsg);
        else if(form == FormType.CONTACT_US_FORM)
            elements = selenium.findElementsByXpath(mainPageObjects.contactUsErrorMsgs);
        else
            elements = selenium.findElementsByXpath(mainPageObjects.popupErrorMsgs);

        selenium.verifyElementText(elements.get(0),illegalEmailMsg);
        selenium.verifyElementText(elements.get(1),illegalPhoneMsg);
    }

    /**
     * The function creates error messages for missing required form fields.
     * @param errorFields - The missing required text field value.
     * @return A list of error messages.
     */
    private List<String> fillErrorList(List<String>errorFields){
        List<String> newErrors = new ArrayList<>();
        for (String error : errorFields)
            newErrors.add(requiredFieldMsg.replace("*",error));
        return newErrors;
    }

    /**
     * The function verifies the integrity of the link buttons.
     */
    public void checkPageButtons(){
        List<WebElement> linkElements = mainPageObjects.outsidePageLinks;
        selenium.scrollToElement(linkElements.get(1));

        checkUnbrokenLinks(linkElements,outsidePageLinks);

        //Verify "Back To Top" button.
        selenium.clickOnElement(mainPageObjects.scrollUpBtn);
        sleep(1);
        Assert.isTrue(selenium.getPageYOffset() == 0,"Fail! - Scroll to top button is not working");
    }

    public void verifyPage(){
        selenium.verifyElementText(mainPageObjects.weAreHerolo,weAreHeroloText);
    }

    /**
     * The function gets the popup to load
     */
    public void getPopup(){
        selenium.scrollToElement(mainPageObjects.contactUsSendBtn);
        sleep(popupLoadingTime);
    }

    public void fillPopupForm(String name, String email, String phone){
        selenium.elementSendText(mainPageObjects.popupNameTextBox,name);
        selenium.elementSendText(mainPageObjects.popupEmailTextBox,email);
        selenium.elementSendText(mainPageObjects.popupPhoneTextBox,phone);
    }

    public void sendPopup(){
        selenium.clickOnElement(mainPageObjects.popupSendBtn);
    }

    public void closePopup(){
        selenium.clickOnElement(mainPageObjects.popupCloseBtn);
    }

    public void verifyPopupClosure(){
        Assert.isTrue(!selenium.isElementExists(mainPageObjects.popupTitle),"Error! - popup was not closed");
    }

    public void getToWorkExamples(){
        selenium.scrollToElement(mainPageObjects.workExampleCheckboxs.get(0));
    }

    /**
     * The function tests if the right arrow works.
     */
    public void rightArrowFunctionalityTest(){
        List<WebElement> dotElements = mainPageObjects.workExampleCheckboxs;
        Assert.isTrue(selenium.elementGetClass(dotElements.get(0)).equals(activeDot),"Error! - The left work examples checkbox is not marked.");
        for(int i=0; i<dotElements.size()-1; i++)
            getNextWorkExamples(mainPageObjects.workExampleRightArrow,dotElements.get(i+1),dotElements.get(i));
        getNextWorkExamples(mainPageObjects.workExampleRightArrow,dotElements.get(0),dotElements.get(dotElements.size()-1));
    }

    /**
     * The function tests if the left arrow works.
     */
    public void leftArrowFunctionalityTest(){
        List<WebElement> dotElements = mainPageObjects.workExampleCheckboxs;
        Assert.isTrue(selenium.elementGetClass(dotElements.get(0)).equals(activeDot),"Error! - The left work examples checkbox is not marked.");
        for(int i=dotElements.size()-1; i>0; i--)
            getNextWorkExamples(mainPageObjects.workExampleLeftArrow,dotElements.get(i),dotElements.get((i+1)%5));
        getNextWorkExamples(mainPageObjects.workExampleLeftArrow,dotElements.get(0),dotElements.get(1));
    }

    /**
     * The function gets to the next work examples by clicking the right arrow and verifies it.
     * @param arrow The arrow to click on (Left or Right).
     * @param markedElement - The expected marked element.
     * @param unMarkedElement - The element that was marked before.
     */
    private void getNextWorkExamples(WebElement arrow, WebElement markedElement, WebElement unMarkedElement){
        clickOnArrow(arrow);
        Assert.isTrue(selenium.elementGetClass(markedElement).equals(activeDot),"Error! - the next marked checkbox is not marked.");
        Assert.isTrue(selenium.elementGetClass(unMarkedElement).equals(""),"Error! - the previous checkbox is still marked.");
    }

    private void clickOnArrow(WebElement arrow){
        selenium.clickOnElement(arrow);
        sleep(1);
    }
}
