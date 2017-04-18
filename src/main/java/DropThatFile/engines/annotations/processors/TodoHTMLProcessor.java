package DropThatFile.engines.annotations.processors;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import DropThatFile.engines.annotations._Todo;

@SupportedAnnotationTypes(value = { "DropThatFile.engines.annotations._Todo" }) //Specifie which annotation Recipient to process
@SupportedSourceVersion(SourceVersion.RELEASE_8) //Specifie which jdk source version Recipient to handle
public class TodoHTMLProcessor extends AbstractProcessor {
    List<_Todo> list;
    FileOutputStream fw = null;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        list = new ArrayList<>();

        //Adding annotations into a list
        for (TypeElement te : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(te)) {
                _Todo todo = element.getAnnotation(_Todo.class);

                if (todo != null) {
                    list.add(todo);
                }
            }
        }

        generateHTML(list);
        return true;
    }

    private void generateHTML(List<_Todo> list){

        StringBuilder html = new StringBuilder();
        html.append("<html>");
        html.append("<body>");
        html.append("<center><table>");

        html.append("<tr>");
        html.append("<td style=\"border:2px solid black\">Status</td>");
        html.append("<td style=\"border:2px solid black\">Comment</td>");
        html.append("</tr>");

        Iterator<_Todo>  it = list.iterator();

        if(list.isEmpty())return;

        File htmlFile = new File("Todo.html");

        try {
            fw = new FileOutputStream(htmlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(it.hasNext()){
            _Todo todo = it.next();
            html.append("<tr>");
            String style = "";

            switch(todo.level().toString()){
                case "IMPROVEMENT" :
                    style = "style=\"color:green;border:2px solid black\"";
                    break;
                case "EVOLUTION":
                    style = "style=\"color:purple;border:2px solid black\"";
                    break;
                case "BUG":
                    style = "style=\"color:orange;border:2px solid black\"";
                    break;
                case "CRITICAL":
                    style = "style=\"color:red;border:2px solid black\"";
                    break;
            }

            html.append("<td " + style + "><b>" + todo.level() + "</b></td>");
            html.append("<td " + style + ">" + todo.comment() + "</td>");
            html.append("</tr>");
        }

        html.append("</table></center>");
        html.append("</body>");
        html.append("</html>");

        //Writing this HTML code into a file
        try {
            fw.write(html.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
