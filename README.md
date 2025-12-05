# Agent Systems Project: Reliability Estimation of Multi-State Flow Networks

## Overview
This project implements a Multi-Agent System (MAS) using the **JADE framework** to estimate the reliability of time-constrained multi-state flow networks (MFN) with correlated faults and non-integer capacities.

The system uses Monte Carlo simulation to estimate the probability that a specific amount of flow ($d$) can be transmitted from a source to a sink within a specific time limit ($T$). It involves generating System State Vectors (SSVs) based on component reliability and correlation data.

**Author:** [Your Name]  
**Course:** Agent Systems (GUT – FETI)  
**Supervisor:** Paweł Kozyra

## Project Structure
The project consists of 4 main classes:

1.  **`MFN`**: The core logic class. It handles:
    * Storage of network parameters (Links, Capacity, Lead Time, Reliability, Correlation).
    * Mathematical computations (CDF, Inverse CDF, Combinatorics).
    * Random SSV generation using the Inverse CDF or Chen and Asau method.
2.  **`SSVGenerator` (Agent)**: 
    * Initializes the simulation based on precision parameters ($\epsilon, \delta$).
    * Launches the GUI.
    * Generates random System State Vectors.
    * Communicates with the TT Agent.
3.  **`SSVGeneratorGui` (JFrame)**:
    * User interface for selecting the Minimal Paths (MPs) CSV file.
    * Triggers the data processing.
4.  **`TT` (Agent)**:
    * Responsible for Transmission Time (TT) computation.
    * Receives SSVs from the Generator.
    * Estimates network reliability by comparing transmission times against constraint $T$.
    * Writes generated SSVs to a CSV file.

## Theoretical Background
The implementation is based on the following resources:
* **[1] P. M. Kozyra**: "A dictionary algorithm for the solution to the generalized quickest path reliability problem."
* **[2] G.S. Fishman**: "Monte Carlo, Concepts, Algorithms, and Applications" (Formula 12b for sample size).
* **[3] J. E. Gentle**: "Random Number Generation and Monte Carlo Methods" (Chen and Asau Guide Table Method).

## Prerequisites
* Java JDK (8 or higher)
* JADE Framework (version 4.6.0 or compatible)

## Compilation & Execution

### 1. Compile
Ensure the JADE library is in your classpath.
```bash
javac -cp "path/to/jade.jar;." *.java
```
### 2. Run
Use the JADE Boot class to start the container and agents. You must pass arguments to the agents:

SSVGenerator: takes epsilon and delta (e.g., 0.01,0.01).

TT: takes flow units d and max time T (e.g., 42,15.5).

Command Line Example:

``` bash

java -cp "path/to/jade.jar;classes" jade.Boot -gui -agents "SSVAgent:SSVGenerator(0.01,0.01);TTAgent:TT(42,15.5)"
```
#### Input File Format
The system requires a CSV file containing the Minimal Paths (MPs).

Format: Comma-separated strings representing edges in a path.

Example (MPs0.csv):
``` 
a1,a2
a1,a3,a5
a4,a3,a2
a4,a5
```