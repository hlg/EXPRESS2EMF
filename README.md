# EXPRESS2EMF

## Structure

* __ExpressParser__: XText based parser for EXPRESS schema
* __ExpressEMF__: Emoflon TGG to convert EXPRESS model to Ecore model
* __de.htwdd.expressEMF.step__: XText based parser for SPF files (instances of EXPRESS model)
* __de.htwdd.expressEMF.step.ide__: Eclipse SPF editor
* __de.htwdd.expressEMF.step.ui__: Eclipse UI components SPF editor
* __IFC2CityGML__: Emoflon TGG to convert IFC4 to CityGML 3



## How to

### Set up

1. Install Eclipse modelling, Emoflon ...
    - TODO download, installation
    - TODO Emoflon perspective
2. Checkout repository
3. Import projects in Eclipse: _File / Import Projects from File System or Archive_
    - select all child projects, skip root
    - Check option _Detect and configure project natures_
4. Build ExpressParser project
5. Build ExpressEMF project


### Run example

1. ExpressParser:
    - Run `de.htwwdd.expressEMF.api.RunParser` (optional: schema location as argument, default schemas/IFC4.exp).
    - This will create an *.xmi file in the same location as the original *.exp file, for instance schemas/IFC4.xmi for the default.
2. EXPRESSEMF: 
    - Copy the *.xmi file generated in step 1 to instances/src.xmi to be used as input for the transformation.
    - Run `org.emoflon.ibex.tgg.run.expressemf.INITIAL_FWD_App` to carry out the transformation.
    - This will create a file instances/trg.xmi as result of the transformation.
3. IFC2CityGML:
    - Copy the *.xmi file generated in step 2 to model/IFC4.ecore
    - TODO complete

work in progress to verify and document installation, build and execution 




