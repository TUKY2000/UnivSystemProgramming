package com.tukY.Lab2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.stream.Collectors;
import java.util.*;

public class FiniteStateMachine {
    //region Fields
    private State state;
    private Map<Integer, State> states;
    private String alphabet;

    private int count_alphabet;
    private int count_states;

    private State init_state;
    //endregion

    //region Constructors

    public FiniteStateMachine(String filepath){
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))){
            readMachine(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FiniteStateMachine(List<State> states, String transitions, int start){

    }

    private FiniteStateMachine(FiniteStateMachine machine) {
        this.state = machine.state;
        this.init_state = machine.init_state;
        this.states = machine.states;
        this.count_states = machine.count_states;
        this.count_alphabet = machine.count_alphabet;
        this.alphabet = machine.alphabet;
    }
    //endregion


    //region Methods

    public FiniteStateMachine append(int state0, char transition, int state1){
        FiniteStateMachine fsm = new FiniteStateMachine(this);
        fsm.states.get(state0).addTransition(transition, fsm.states.get(state1));
        return fsm;
    }

    public State getCurrentState() {
        return state;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public void exec(String word){
        if (!isPossible(word))
            throw new InvalidParameterException("Invalid word");

        for (char c: word.toCharArray())
            exec(c);
    }

    public void exec(char c) throws InvalidParameterException{
        if(!isPossible(c))
            throw new InvalidParameterException("Current state" + state +
                    " has not transition : " + c);

        this.state = state.exec(c);
    }

    public boolean isPossible(String word){
        if (word.isEmpty())
            return state.isFinal();

        for (char c : word.toCharArray())
            if(!isPossible(c))
                return false;
        return true;
    }

    public boolean isPossible(char c){
        return state.isPossible(c);
    }

    public void reset(){
        state = init_state;
    }

    //endregion
    //region Private Functions

    private void readMachine(BufferedReader reader) throws IOException {
        count_alphabet = Integer.parseInt(reader.readLine());
        count_states = Integer.parseInt(reader.readLine());

        int start = Integer.parseInt(reader.readLine());

        // read final states
        Set<Integer> fstates = Arrays.stream(reader.readLine().split(" "))
                .skip(1)
                .map(Integer::parseInt)
                .collect(Collectors.toSet());

        states = getStates(reader, fstates);

        state = init_state = states.get(start);
    }

    private Map<Integer, State> getStates(BufferedReader reader, Set<Integer> fstates)
            throws IOException, InvalidParameterException {
        Map<Integer, State> states = new HashMap<>();

        String str;
        String[] tfn;

        State state0
            , state1;

        alphabet = "";
        String transition;
        while ((str = reader.readLine()) != null){
            if (str.isEmpty())
                continue;

            tfn = str.split(" ");

            for (String c : tfn[1].split(""))
                if (!alphabet.contains(c))
                    alphabet = alphabet.concat(c);

            if (alphabet.length() > count_alphabet)
                throw new InvalidParameterException("Invalid FSM description");

            state0 = states.computeIfAbsent(Integer.valueOf(tfn[0])
                    , key -> new State(key, fstates.contains(key)));
            state1 = states.computeIfAbsent(Integer.valueOf(tfn[2])
                    , key -> new State(key, fstates.contains(key)));

            transition = tfn[1];
            state0.addTransition(transition, state1);
        }
        return states;
    }
    //endregion

    //region Inner Classes

    public static class State{
        //region Fields
        private final int state;
        private final boolean isFinal;
        private final Map<Character, State> transitions;
        //endregion

        //region Constructors
        public State(int state, boolean isFinal){
            this.state = state;
            this.isFinal = isFinal;
            this.transitions = new HashMap<>();
        }

        public State(int state){
            this(state, false);
        }
        //endregion

        //region Methods
        public int state(){
            return state;
        }

        public boolean isFinal(){
            return isFinal;
        }

        public boolean isPossible(char c){
            return transitions.containsKey(c);
        }

        public State exec(char c){
            return transitions.get(c);
        }

        public void addTransition(char transition, State state){
            transitions.putIfAbsent(transition, state);
        }

        public void addTransition(String transition, State state){
            char t;
            for (int i = 0; i < transition.length(); i++) {
                if ((t = transition.charAt(i)) == '\\') {
                    String s = String.valueOf(t) + transition.charAt(++i);
                    t = s.charAt(0);
                }
                else if (t == '0' && transition.charAt(i+1) == 'x'){
                    StringBuilder code = new StringBuilder(String.valueOf(t));
                    code.append(transition.charAt(++i));
                    while (Character.isDigit(transition.charAt(i+1)))
                        code.append(transition.charAt(++i));
                    t = getCharFromCode(Integer.parseInt(code.toString()));
                }
                addTransition(t, state);
            }
        }
        //endregion

        private char getCharFromCode(int code){
            return (char) code;
        }
    }
    //endregion
}
