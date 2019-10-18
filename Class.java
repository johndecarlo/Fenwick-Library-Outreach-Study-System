public class Class {

   private String subject;
   private String subjectCode;
   private int number;
   private String title;
   
   public Class() {
      this.subject = "";
      this.subjectCode = "";
      this.number = 0;
      this.title = "";
   }
   
   public Class(String subject, String subjectCode, int number, String title) {
      this.subject = subject;
      this.subjectCode = subjectCode;
      this.number = number;
      this.title = title;
   }
   
   public String getSubject() {
      return this.subject;
   }
   
   public void setSubject(String subject) {
      this.subject = subject;
   }
   
   public String getSubjectCode() {
      return this.subjectCode;
   }
   
   public void setSubjectCode(String subjectCode) {
      this.subjectCode = subjectCode;
   }
   
   public int getNumber() {
      return this.number;
   }
   
   public void setNumber(int number) {
      this.number = number;
   }
   
   public String getTitle() {
      return title;
   }
   
   public void setTitle(String title) {
      this.title = title;
   }
   
   public String toString() {
      return subject + " " + number + ": " + title;
   }
}