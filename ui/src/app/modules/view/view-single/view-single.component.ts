import {
  Component,
  OnChanges,
  OnDestroy,
  OnInit,
  SimpleChanges,
  ViewChild,
} from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { Subscription } from "rxjs";
import { HumanService } from "@services/human.service";
import { SensorService } from "@services/sensor.service";
import { DataTableComponent } from "@components/data-table/data-table.component";
import { SensorMetadataService } from "@services/sensorMetadata.service";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { SafeUrl } from "@angular/platform-browser";
import * as L from "leaflet";
import {
  DEFAULT_CENTER,
  DEFAULT_LAT,
  LEAFLET_ATTRIBUTION_SMALL,
  LEAFLET_URL,
} from "src/app/constantes";
import { CLICK_MARKER } from "@models/icon.model";
import { AuthService } from "@services/auth.service";

const widths = {
  INTEGER: 150,
  DOUBLE: 150,
  STRING: 300,
  DATE: 180,
};

@Component({
  selector: "app-view-single",
  templateUrl: "./view-single.component.html",
  styleUrls: ["./view-single.component.scss"],
})
export class ViewSingleComponent implements OnInit, OnDestroy {
  id?: number = 0;
  metadata?: SensorMetadata;
  editable: boolean = false;
  lnglat: Pos = DEFAULT_CENTER;

  // sensor: SensorExtended | undefined;
  parameters?: RecordParameters;
  records: (string | number)[][];
  edition: boolean = false;
  sensorsMetadata: SensorMetadata[] = [];
  sensorForm: FormGroup;
  defaultUrl: SafeUrl;
  photo?: File;
  formater: DTFormatter<(string | number)[]> = (d) => d;
  create: boolean;

  marker: L.Marker;
  private map: L.Map;

  private routeSub: Subscription;

  @ViewChild(DataTableComponent, { static: true })
  public tb: DataTableComponent<string | number>;

  constructor(
    private formBuilder: FormBuilder,
    private sensorService: SensorService,
    private sensorMetadataService: SensorMetadataService,
    public humanService: HumanService,
    private route: ActivatedRoute,
    private router: Router,
    public auth: AuthService
  ) {}

  /**
   * All static initializations
   */
  staticInit(): void {
    this.initForm();
    this.initMap();
  }

  /**
   * All initializations relative to router changes
   */
  dynamicInit(): void {
    this.routeSub = this.route.params.subscribe((params) => {
      if (params["id"] == "create") {
        this.id = undefined;
        if (this.auth.isUser()) this.dynamicInitCreate();
        else this.router.navigate(["/login"]);
      } else {
        let id = +params["id"];
        this.id = id;
        this.dynamicInitUpdate();
      }
    });
  }

  dynamicInitCreate() {
    if (this.id != undefined) throw Error("Can't Create during update");
    this.enableMap();
    this.create = true;
    this.edition = true;
    this.sensorForm.enable();
    this.sensorForm.patchValue({
      sensorMetadata: undefined,
      name: "NEW",
      displayName: "Nouveau capteur",
      subDisplayName: "Sub dsiplay",
      longitude: 0.0,
      latitude: 0.0,
      hiddenMessage: "Ce capetru n'est pas disponible pour le moment",
      isHidden: 0,
      description: "",
      maintenanceDescription: "",
    });

    this.humanService
      .getLocation()
      .subscribe((pos) => this.markMap(pos), console.error);
  }

  dynamicInitUpdate() {
    if (this.id == undefined) throw Error("Can't Update during creation");
    this.disableMap();
    this.create = false;
    this.sensorForm.disable();
    this.sensorService
      .getImage(this.id)
      .subscribe((url) => (this.defaultUrl = url), console.error);

    this.sensorService.getById(this.id).subscribe(
      (sensor) => {
        this.markMap({ lat: sensor.latitude, lng: sensor.longitude });
        this.metadata = sensor.metadata;
        this.editable = sensor.manageable;
        this.sensorForm.patchValue({
          sensorMetadataId: sensor.metadata.id,
          name: sensor.name,
          displayName: sensor.displayName,
          subDisplayName: sensor.subDisplayName,
          longitude: sensor.longitude.toFixed(5),
          latitude: sensor.latitude.toFixed(5),
          hiddenMessage: sensor.hiddenMessage,
          isHidden: sensor.isHidden,
          description: sensor.description,
          maintenanceDescription: sensor.maintenanceDescription,
        });
      },
      (err) => {
        if (err.status == 403) {
          this.router.navigate(["/view"]);
        } else {
          console.log("Error during sensor fetch");
          console.log(err);
        }
      }
    );
    this.tb.onChange();
  }

  initForm(): void {
    this.sensorsMetadata = [];
    this.sensorMetadataService.getAll().subscribe(
      (res) => {
        res.forEach((smd) => this.sensorsMetadata.push(smd));
      },
      (err) => {
        console.error("Error during sensorMetdata fetch");
        console.error(err);
      }
    );
    this.sensorForm = this.formBuilder.group({
      sensorMetadata: ["", Validators.required],
      name: ["", Validators.required],
      displayName: ["", Validators.required],
      subDisplayName: ["", Validators.required],
      hiddenMessage: ["", Validators.required],
      isHidden: [""],
      latitude: ["", Validators.required],
      longitude: ["", Validators.required],
      description: ["", Validators.required],
      maintenanceDescription: ["", Validators.required],
      photo: [""],
    });
  }

  /**
   * Initalize litle zone for sensor.
   */
  initMap(): void {
    this.map = L.map("map-view-single", {
      center: DEFAULT_CENTER,
      zoom: 3,
      zoomControl: false,
    });
    this.disableMap();
    const tiles = L.tileLayer(LEAFLET_URL, {
      attribution: LEAFLET_ATTRIBUTION_SMALL,
    });
    tiles.addTo(this.map);
    this.map.on("click", (event: any) => {
      if (!this.create) return;
      const { lat, lng } = event.latlng;
      this.markMap({
        lat: Math.round(lat * 100000) / 100000,
        lng: Math.round(lng * 100000) / 100000,
      });
    });
  }

  disableMap() {
    this.map.dragging.disable();
    this.map.touchZoom.disable();
    this.map.doubleClickZoom.disable();
    this.map.scrollWheelZoom.disable();
    this.map.boxZoom.disable();
    this.map.keyboard.disable();
    if (this.map.tap) this.map.tap.disable();
    const mapRef = document.getElementById("map-view-single");
    if (mapRef?.style?.cursor) {
      (<any>mapRef).style.cursor = "default";
    }
    this.map.setMaxZoom(3);
    this.map.setMinZoom(3);
  }

  enableMap() {
    this.map.dragging.enable();
    this.map.touchZoom.enable();
    this.map.doubleClickZoom.enable();
    this.map.scrollWheelZoom.enable();
    this.map.boxZoom.enable();
    this.map.keyboard.enable();
    if (this.map.tap) this.map.tap.enable();
    const mapRef = document.getElementById("map-view-single");
    if (mapRef?.style?.cursor) {
      (<any>mapRef).style.cursor = 'url("/assets/markers/marker-gray.png") 12 41, pointer !important';
    }
    this.map.setMaxZoom(18);
    this.map.setMinZoom(1);
  }

  /**
   * Remove previous marker and add one to lat and lng then move the map to the coords.
   * @param {lat, lng}: {lat: number, lng: number}
   */
  markMap({ lat, lng }: { lat: number; lng: number }) {
    if (this.marker) this.map.removeLayer(this.marker);
    this.marker = L.marker({ lat, lng }, { icon: CLICK_MARKER });
    this.marker.addTo(this.map);
    this.map.flyTo({ lat, lng });
    this.sensorForm.controls["latitude"].setValue(lat.toFixed(5));
    this.sensorForm.controls["longitude"].setValue(lng.toFixed(5));
    this.map.invalidateSize();
  }

  /**
   * Initalize component.
   */
  ngOnInit(): void {
    this.staticInit();
    this.dynamicInit();
  }

  /**
   * Clear All.
   */
  ngOnDestroy(): void {
    this.routeSub?.unsubscribe();
    this.map.remove();
  }

  /**
   * Handler for pagination change on datatable
   *
   * @param info information about event
   */
  onChange(info: DataTableNatigateEvent) {
    if (this.id == undefined)
      throw Error("Can't change datatable info during create");
    this.sensorService
      .getParametersOfId(this.id, info)
      .subscribe((parameters) => {
        this.parameters = parameters;
      });
  }

  onChangeMetadata(event: SensorMetadata) {
    console.log(event);
    this.metadata = event;
  }

  /**
   * Trigger on button after edit for save changes.
   */
  onSave() {
    this.edition = false;
    this.sensorForm.disable();
    const form = Object.assign({}, this.sensorForm.value);
    const data = new FormData();
    if (this.photo) data.append("photo", this.photo, "photo.png");

    data.append(
      "sensor",
      new Blob(
        [
          JSON.stringify({
            sensorMetadataId: form["sensorMetadata"].id,
            name: form["name"],
            displayName: form["displayName"],
            subDisplayName: form["subDisplayName"],
            longitude: +form["longitude"],
            latitude: +form["latitude"],
            hiddenMessage: "Aucun",
            isHidden: false,
            description: form["description"],
            maintenanceDescription: form["maintenanceDescription"],
          }),
        ],
        {
          type: "application/json; charset=utf-8",
        }
      )
    );

    if (this.create)
      this.sensorService.create(data).subscribe((sensor) => {
        console.log(sensor);
        this.router.navigate(["/view", sensor.id]);
      }, console.error);
    else
      this.sensorService
        .update(<number>this.id, data)
        .subscribe(console.log, console.error);
  }

  /**
   * Switch to edition mode.
   */
  onEdition() {
    this.edition = !this.edition;
    if (this.edition) {
      this.sensorForm.enable();
      this.sensorForm.controls.sensorMetadata.disable();
      this.sensorForm.controls.latitude.disable();
      this.sensorForm.controls.longitude.disable();
    } else {
      this.sensorForm.disable();
    }
  }

  /**
   * Run download for csv file.
   */
  onDownloadCSV(): void {
    if (this.id == undefined) return;
    this.sensorService
      .download({ sensorId: this.id, type: "csv" })
      .subscribe(() => {}, console.error);
  }

  /**
   * Run download for json file.
   */
  onDownloadJSON() {
    if (this.id == undefined) return;
    this.sensorService
      .download({ sensorId: this.id, type: "json" })
      .subscribe(() => {}, console.error);
  }

  /**
   * function used by datatable for format.
   * @param index index of row
   * @param value value of row
   * @returns Value usable by datatable
   */
  format(index: number, value: string | number): string | Icon {
    if (this.type(index) === "DOUBLE") return Number(value).toExponential(3);
    if (value === null || value === undefined) return "";
    return value.toString();
  }

  /**
   * Widths of Datatable row.
   * @returns
   */
  sizes() {
    let width = this.parameters?.metadata.width || 0;
    let type = this.parameters?.metadata.formats || [];
    let arr = [];
    for (let i = 0; i < width; i++) {
      arr.push(widths[type[i]]);
    }
    return arr;
  }

  /**
   * Return parameter type of given index.
   * @param index index in meta parameter.
   * @returns type as string.
   */
  type(index: number): "INTEGER" | "DOUBLE" | "STRING" | "DATE" | undefined {
    return this.parameters?.metadata.formats[index];
  }

  /**
   * Create link to google map point from lat and lng.
   * @returns The link
   */
  coordLink(): string {
    let base = "https://www.google.com/maps/search/?api=1";
    const form = Object.assign({}, this.sensorForm.value);
    return `${base}&query=${form["latitude"]},${form["longitude"]}`;
  }
}
