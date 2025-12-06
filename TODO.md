# Agent Systems Project TODO List (Total: 50 Points)

## PHASE 1: MFN Class (Logic & Math) - [25 pts total]
*Ref: P. M. Kozyra, "A dictionary algorithm...", Formulae (1), (3)-(5), (8)*

- [x] **[1 pt]** Implement Field Definitions
    - `int m`, `int[] W`, `double[] C`, `int[] L`, `double[] R`, `double[] rho`, `double[] beta`
    - `ArrayList MPs`
- [x] **[2 pts]** Implement Inner Class `Combinatorial`
    - `factorial(n)`
    - `binomialCoefficient(n, k)`
- [x] **[2 pts]** Implement Constructor
    - Check if vector lengths == `m`
    - Check if `R` and `rho` values are between 0 and 1
    - Create `beta` vector (Formula 2)
- [x] **[5 pts]** Implement Core Formulae Methods
    - Implement methods for formulae (1), (3)-(5), and (8) from [1]
- [x] **[3 pts]** Implement `getMPs(String fileName)`
    - Read CSV file and populate `MPs` ArrayList
- [x] **[1 pt]** Implement `CDF(double[][] arPMF)`
    - Create array of values for the cumulative distribution function
- [x] **[2 pts]** Implement `static double normalCDF(double z)`
    - Approximate standard normal distribution (n=100) using double factorial (!!)
- [x] **[5 pts]** Implement `static double normalICDF(double u)`
    - **Important:** Invent/use custom algorithm for Quantile function
    - Constraint: `|normalCDF(x) - u| <= 10^-7`
- [ ] **[1 pt]** Implement Sample Size Calculator
    - Based on Fishman Formula (12b) [2]
- [ ] **[3 pts]** Implement `randomSSV(int N, double[][] arCDF)`
    - Generate `N` random SSVs
    - Use Inverse CDF method or Chen and Asau Guide Table Method [3]

## PHASE 2: SSVGeneratorGui & Agent Setup - [7 pts total]

- [ ] **[2 pts]** Implement SSVGenerator Agent Setup
    - Parse arguments: `epsilon`, `delta`
    - Validate that values are between 0 and 1 (terminate if not)
- [ ] **[5 pts]** Implement SSVGeneratorGui (extends JFrame)
    - File selection for MPs (`.csv`)
    - Input/Display mechanism for MFN parameters
    - "Send Data" button to trigger the agent logic

## PHASE 3: SSVGenerator Agent Logic - [10 pts total]

- [ ] **[1 pt]** Handle "Send Data" Action
    - Create `MFN` object
    - Display MFN parameters (`W`, `C`, `L`, `R`, `rho`) in console
- [ ] **[0.5 pts]** Generate Data
    - Call `randomSSV` to generate `N` vectors
- [ ] **[0.5 pts]** Service Discovery
    - Search for the "TT" agent (Transmission Times agent)
- [ ] **[6 pts]** Implement Communication (Sender)
    - Send `ACLMessage` to TT Agent containing:
        1. MFN parameters
        2. Path to MPs `.csv` file
        3. Generated Random SSVs
- [ ] **[2 pts]** Implement Receive & Terminate
    - Wait for reply from TT Agent
    - Display the "Estimated network reliability"
    - Terminate agent (`doDelete()`)

## PHASE 4: TT Class (Transmission Time Agent) - [8 pts total]

- [ ] **[1 pt]** Implement TT Agent Setup
    - Parse arguments: `double d` (flow units), `double T` (max time)
- [ ] **[6 pts]** Implement Receive & File I/O
    - Receive message from SSVGenerator
    - Write the received random SSVs to `SSV.csv`
- [ ] **[0.5 pts]** Calculate Transmission Times
    - For each SSV `X`, compare `T(d, X)` with max time `T` (Formula 8)
- [ ] **[0.5 pts]** Estimate Reliability & Reply
    - Approximate network reliability (success count / total N)
    - Send result back to SSVGenerator

## PHASE 5: Validation & Testing

- [ ] **Verify Network 1**
    - Args: `SSV(0.01, 0.01)`, `TT(42, 15.5)`
    - MPs: `MPs0.csv`
    - Target Reliability: ~0.895
- [ ] **Verify Network 2**
    - Args: `SSV(0.01, 0.01)`, `TT(36, 120)`
    - MPs: `MPs1.csv`
    - Target Reliability: ~0.842
- [ ] **Check Output**
    - Ensure `SSV.csv` (or `SSVs0.csv`/`SSVs1.csv`) is created and populated.