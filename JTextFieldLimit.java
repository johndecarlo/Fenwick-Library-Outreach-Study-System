/*
   StudySystemMap.java
   CS 321 - Section 001: Team 7
   John DeCarlo, Huiying Jin, John Radecki, Joshua Yuen
   ----------------------------------------------------
   Description: Alternate class of the JTextField class that
   sees the number of characters used being limited
*/

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;

class JTextFieldLimit extends PlainDocument {
   private int limit;
   
   JTextFieldLimit(int limit) {
      super();
      this.limit = limit;
   }
   
   JTextFieldLimit(int limit, boolean upper) {
      super();
      this.limit = limit;
   }
   
   public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
      if (str == null)
         return;
      if ((getLength() + str.length()) <= limit) {
         super.insertString(offset, str, attr);
      }
   }
}