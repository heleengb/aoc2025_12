package software.ulpgc.aoc.view;

public class SolutionDisplay implements ResultPresenter {
    @Override
    public void display(String title, long result) {
        System.out.println(title);
        System.out.println("Resultado: " + result);
    }
}