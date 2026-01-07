package university.registration.ui.components;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Date;

/**
 * JSpinner với DateEditor và hỗ trợ placeholder text
 */
public class PlaceholderDateSpinner extends JPanel {
    private JSpinner spinner;
    private JFormattedTextField textField;
    private String placeholder;

    public PlaceholderDateSpinner(String format, String placeholder) {
        this.placeholder = placeholder;
        
        setLayout(new BorderLayout());
        setOpaque(false);
        
        spinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinner, format);
        spinner.setEditor(dateEditor);
        
        textField = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        textField.setEditable(false);
        
        // Custom text field để hiển thị placeholder
        JFormattedTextField customTextField = new JFormattedTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                String displayText = textField.getText();
                if ((placeholder != null && placeholder.length() > 0) && 
                    (displayText == null || displayText.trim().isEmpty() || displayText.equals(format))) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(156, 163, 175)); // Gray color for placeholder
                    g2.setFont(getFont().deriveFont(Font.PLAIN));
                    
                    Insets insets = getInsets();
                    int x = insets.left + 12;
                    int y = (getHeight() - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent();
                    
                    g2.drawString(placeholder, x, y);
                    g2.dispose();
                }
            }
        };
        
        add(spinner, BorderLayout.CENTER);
        
        // Lắng nghe thay đổi để repaint placeholder
        spinner.addChangeListener(e -> repaint());
        textField.addPropertyChangeListener("value", e -> repaint());
    }
    
    public JSpinner getSpinner() {
        return spinner;
    }
    
    public Date getValue() {
        return (Date) spinner.getValue();
    }
    
    public void setValue(Date date) {
        if (date != null) {
            spinner.setValue(date);
        } else {
            spinner.setValue(null);
            if (textField != null) {
                textField.setText("");
            }
        }
        repaint();
    }
    
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }
    
    public String getPlaceholder() {
        return placeholder;
    }
    
    public void setFont(Font font) {
        super.setFont(font);
        if (spinner != null) {
            spinner.setFont(font);
            if (textField != null) {
                textField.setFont(font);
            }
        }
    }
    
    public void setPreferredSize(Dimension d) {
        super.setPreferredSize(d);
        if (spinner != null) {
            spinner.setPreferredSize(d);
        }
    }
    
    public void setMaximumSize(Dimension d) {
        super.setMaximumSize(d);
        if (spinner != null) {
            spinner.setMaximumSize(d);
        }
    }
    
    public void setBorder(Border border) {
        super.setBorder(border);
        if (spinner != null) {
            spinner.setBorder(border);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (textField != null) {
            String displayText = textField.getText();
            if ((placeholder != null && placeholder.length() > 0) && 
                (displayText == null || displayText.trim().isEmpty())) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(156, 163, 175)); // Gray color for placeholder
                g2.setFont(textField.getFont().deriveFont(Font.PLAIN));
                
                Rectangle bounds = textField.getBounds();
                Insets insets = textField.getInsets();
                int x = bounds.x + insets.left + 12;
                int y = bounds.y + (bounds.height - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent();
                
                g2.drawString(placeholder, x, y);
                g2.dispose();
            }
        }
    }
}

