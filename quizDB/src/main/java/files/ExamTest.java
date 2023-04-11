package files;

import java.util.List;

public class ExamTest extends Exam {

    public String examName;
    public int examId;
    public List<Question> question;


    public ExamTest(String examName, int examId, List<Question> question) {
        this.examName = examName;
        this.examId = examId;
        this.question = question;
    }

    public ExamTest() {

    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public List<Question> getQuestion() {
        return question;
    }

    public void setQuestions(List<Question> question) {
        this.question = question;
    }

    @Override
    public String getExamName() {
        return examName;
    }


   @Override
    public void setExamName(String examName) {
        this.examName = examName;


    }

    @Override
    public String toString() {
        return "ExamTest{" +
                "examName='" + examName + '\'' +
                ", examId='" + examId + '\'' +
                ", questions=" + question +
                '}';
    }
}
