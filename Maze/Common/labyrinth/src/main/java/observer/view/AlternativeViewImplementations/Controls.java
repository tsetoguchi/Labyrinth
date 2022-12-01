//package observer.view.AlternativeViewImplementations;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.List;
//import java.util.Optional;
//import javax.swing.JButton;
//import javax.swing.JFileChooser;
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import json.JsonSerializer;
//
//public class Controls extends JPanel {
//
//  List<ObserverGameProjection> states;
//  private int currentState;
//  private final JButton nextButton;
//  private final JButton saveButton;
//
//  public Controls(List<ObserverGameProjection> states) {
//    this.states = states;
//    this.currentState = 0;
//    this.nextButton = this.initializeNextButton();
//    this.saveButton = this.initializeSaveButton();
//
//  }
//
//  private JButton initializeNextButton() {
//    JButton next = new JButton("Next");
//    next.setEnabled(!(this.currentState == this.states.size() - 1));
//    next.addActionListener(e -> {
//      this.currentState++;
//    });
//    return next;
//  }
//
//  private JButton initializeSaveButton() {
//    JButton save = new JButton("Save");
//    save.addActionListener(e -> {
//      String encodedState = this.encodeState();
//      Optional<String> selectedFilePath = this.selectFile();
//      if (selectedFilePath.isPresent()) {
//        this.saveToFile(selectedFilePath.get(), encodedState);
//      }
//
//    });
//
//    return save;
//  }
//
//  private String encodeState() {
//    JsonSerializer serializer = new JsonSerializer();
//
//    ObserverGameProjection currentState = this.states.get(this.currentState);
//    try {
//      return serializer.observerGameToJson(currentState);
//    } catch (JsonProcessingException e) {
//      throw new IllegalStateException(e);
//    }
//  }
//  private void saveToFile(String path, String value) {
//    System.out.println("Trying to save to " + path);
//    try {
//      File newFile = new File(path);
//      newFile.createNewFile();
//      FileWriter writer = new FileWriter(path);
//      writer.write(value);
//      writer.close();
//      System.out.println("Successfully saved to file " + path);
//    } catch (IOException e) {
//      System.out.println("Failed to write to file" + path);
//    }
//  }
//
//  public Optional<String> selectFile() {
//    JFrame saveFrame = new JFrame();
//
//    JFileChooser fileChooser = new JFileChooser();
//    fileChooser.setDialogTitle("Specify a file to save the state in");
//
//    int userSelection = fileChooser.showSaveDialog(saveFrame);
//    File saveFile;
//
//    if (userSelection == JFileChooser.APPROVE_OPTION) {
//      saveFile = fileChooser.getSelectedFile();
//      return Optional.of(saveFile.getAbsolutePath());
//    }
//    return Optional.empty();
//  }
//
//  public void update() {
//    this.saveButton.setEnabled(this.states.size() - 1 == this.currentState);
//    this.nextButton.setEnabled(this.currentState < this.states.size() - 1);
//  }
//
//}
