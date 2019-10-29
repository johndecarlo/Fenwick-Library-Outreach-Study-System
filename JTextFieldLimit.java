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
/*
setLayout(new FlowLayout());
label = new JLabel("max 10 chars");
textfield = new JTextField(15);
add(label);
add(textfield);
textfield.setDocument(new JTextFieldLimit(10));
*/